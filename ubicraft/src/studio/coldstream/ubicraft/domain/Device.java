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

public interface Device {
		
	public String getName();
	
	public Integer getLastValue();
	
	public Set<Method> getSupportedMethods();
	
	public boolean isLearnable();
	
	public Response dim(int level);
	
	public Response turnOn();
	
	public Response turnOff();
	
	public Response toggle();
	
	public Response learn();
	
	public boolean nextControl();
	
	public Method getCurrentControl();
	
	public boolean isControlChanged();
	
	public void setControlChanged(boolean controlChanged);
	
	public boolean isPhysicalDevice();
	
	public Integer getId();

	public void setControl(Method control);

}
