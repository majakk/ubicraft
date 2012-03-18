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

import java.util.HashSet;
import java.util.TreeSet;

public class Group extends AbstractDevice {

	private HashSet<AbstractDevice> devices;
	private Integer lastValue;
	private final int id;

	public Group(int id, String name) {
		super();
		this.id = id;
		devices = new HashSet<AbstractDevice>();
		supportedMethods = null;
		this.name = name;
	}

	public Response dim(int level) {
		if (supportedMethods != null && supportedMethods.contains(Method.TELLSTICK_DIM)) {
			for (AbstractDevice device : devices) {
				Response response = device.dim(level);
				if (!response.isOk())
					return response;
			}
			lastValue = level;
			return Response.succededResponse();
		} else
			return Response.errorResponse(901, "Method not supported");
	}

	public Integer getLastValue() {
		return lastValue;
	}

	public boolean isLearnable() {
		return false;
	}

	public Response learn() {
		return Response.errorResponse(901, "Method not supported");
	}

	public Response turnOff() {
		if (supportedMethods != null && supportedMethods.contains(Method.TELLSTICK_TURNOFF)) {
			for (AbstractDevice device : devices) {
				Response response = device.turnOff();
				if (!response.isOk())
					return response;
			}
			return Response.succededResponse();
		} else
			return Response.errorResponse(901, "Method not supported");
	}

	public Response turnOn() {
		if (supportedMethods != null && supportedMethods.contains(Method.TELLSTICK_TURNON)) {
			for (AbstractDevice device : devices) {
				Response response = device.turnOn();
				if (!response.isOk())
					return response;
			}
			return Response.succededResponse();
		} else
			return Response.errorResponse(901, "Method not supported");
	}
	
	public Response toggle() {
		if (supportedMethods != null && supportedMethods.contains(Method.TELLSTICK_TOGGLE)) {
			for (AbstractDevice device : devices) {
				Response response = device.toggle();
				if (!response.isOk())
					return response;
			}
			return Response.succededResponse();
		} else
			return Response.errorResponse(901, "Method not supported");
	}

	public void addDevice(AbstractDevice device) {
		if (device != null) {
			devices.add(device);
			reviseSupportedMethods(device);
			if(currentControl == null || !supportedMethods.contains(currentControl))
				nextControl();
		}
	}

	private void reviseSupportedMethods(AbstractDevice device) {
		if(supportedMethods == null) {
			supportedMethods = new TreeSet<Method>();
			supportedMethods.addAll(device.getSupportedMethods());
		}
		supportedMethods.retainAll(device.getSupportedMethods());
	}

	public boolean isPhysicalDevice() {
		return false;
	}

	public Integer getId() {
		return id;
	}

	public void dispose() {
		devices.clear();
	}
	
	public boolean contains(AbstractDevice device) {
		return devices.contains(device);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void clear() {
		devices.clear();
	}

	public boolean removeDevice(AbstractDevice physicalDevice) {
		boolean removed = devices.remove(physicalDevice);
		if(removed) {
			reviseSupportedMethods();
			if(!supportedMethods.contains(currentControl))
				nextControl();
		}
		return removed;
	}

	public boolean isEmpty() {
		return devices.isEmpty();
	}

	private void reviseSupportedMethods() {
		TreeSet<Method> newset = null;
		for (AbstractDevice device : devices) {
			if (newset == null)
				newset = new TreeSet<Method>(device.getSupportedMethods());
			else
				newset.retainAll(device.getSupportedMethods());
		}
		if(newset == null)
			supportedMethods = new TreeSet<Method>();
		else
			supportedMethods = newset;
	}
}
