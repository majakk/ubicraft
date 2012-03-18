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

public class Response {

	final private boolean ok;

	final private int responseCode;
	
	final private String errorMsg;

	final private String additionalInformation;

	final private Throwable e;
	
	public static Response succededResponse() {
		return new Response(true, 200);
	}
	
	public static Response errorResponse(int responseCode, String errorMsg) {
		return new Response(false, responseCode, errorMsg, null, null);
	}
	
	public static Response errorResponse(int responseCode, String errorMsg, String additionalInformation, Throwable e) {
		return new Response(false, responseCode, errorMsg, additionalInformation, e);
	}
	
	public static Response errorResponse(int responseCode, String errorMsg, Throwable e) {
		return new Response(false, responseCode, errorMsg, null, e);
	}
	
	private Response(boolean ok, int responseCode, String errorMsg, String additionalInformation, Throwable e) {
		this.ok = ok;
		this.responseCode = responseCode;
		this.errorMsg = errorMsg;
		this.additionalInformation = additionalInformation;
		this.e = e;
	}
	
	private Response(boolean ok, int responseCode) {
		this.ok = ok;
		this.responseCode = responseCode;
		this.errorMsg = "";
		this.additionalInformation = null;
		this.e = null;
	}
	
	public boolean isOk() {
		return ok;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}
	
	public String getAdditionalInformation() {
		return additionalInformation;
	}
		
	public Throwable getThrowable() {
		return e;
	}
}
