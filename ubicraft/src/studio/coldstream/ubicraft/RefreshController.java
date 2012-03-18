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
package studio.coldstream.ubicraft;

import studio.coldstream.ubicraft.domain.Response;
import android.os.AsyncTask;

public class RefreshController extends AsyncTask<MainActivity, Void, Response> {

	private MainActivity rs;

	@Override
	protected Response doInBackground(MainActivity... rs) {
		this.rs = rs[0];
		return this.rs.getController().refresh();
	}

	@Override
	protected void onPostExecute(Response result) {
		super.onPostExecute(result);
		if (!result.isOk()) {
			if (result.getErrorMsg() == null)
				;//rs.showErrorDialog(result.getResponseCode(), "Unknown error", result.getThrowable());
			else
				;//rs.showErrorDialog(result.getResponseCode(), result.getErrorMsg(), result.getThrowable());
			;//rs.onNoConnection();
				
		} else
			//rs.updateDeviceAdapter();
		//rs.dismissActiveDialog();
			;

//		if (result.isOk() && rs.isTimeToVerifyVersion() && rs.getController().getServerVersion() != null) {
//			new VerifyServerVersionTask().execute(rs);
//		}

	}
}
