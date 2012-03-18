/*   
 * Copyright (C) 2010 Patrik Akerfeldt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package studio.coldstream.ubicraft.domain;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class AbstractDevice implements Device {

	protected Method currentControl;
	protected boolean controlChanged;
	/* TODO: Could be changed to a single int instead */
	protected SortedSet<Method> supportedMethods;
	protected String name;

	public AbstractDevice() {
		controlChanged = false;
		supportedMethods = new TreeSet<Method>();
	}
	
	public Set<Method> getSupportedMethods() {
		return supportedMethods;
	}

	public String getName() {
		return this.name;
	}

	public boolean nextControl() {
		boolean goToNextControl = true;
		Method nextControl = currentControl;
		while (goToNextControl) {
			nextControl = getControlAfter(nextControl);

			if (currentControl == nextControl)
				goToNextControl = false;
			if (currentControl == Method.TELLSTICK_TURNOFF && nextControl == Method.TELLSTICK_TURNON)
				continue;
			else if (currentControl == Method.TELLSTICK_TURNON && nextControl == Method.TELLSTICK_TURNOFF)
				continue;
			else if (nextControl == Method.TELLSTICK_LEARN)
				continue;
			else
				goToNextControl = false;

		}

		if (currentControl != nextControl) {
			currentControl = nextControl;
			controlChanged = true;
			return true;
		} else
			return false;
	}

	private Method getControlAfter(Method control) {
		if(supportedMethods == null)
			return Method.TELLSTICK_TURNON;
		Method[] array = supportedMethods.toArray(new Method[supportedMethods.size()]);
		for (int i = 0; i < array.length; i++) {
			if (array[i] == control && i == array.length - 1)
				return array[0];
			else if (array[i] == control)
				return array[i + 1];
		}
		if(supportedMethods.size() > 0)
			return supportedMethods.first();
		else
			return Method.TELLSTICK_TURNON;
	}

	public Method getCurrentControl() {
		return currentControl;
	}

	public boolean isControlChanged() {
		return controlChanged;
	}

	public void setControlChanged(boolean controlChanged) {
		this.controlChanged = controlChanged;
	}

	public void setControl(Method control) {
		if(supportedMethods.contains(control))
			currentControl = control;
	}
	
}
