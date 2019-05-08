/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.webrtc.kite.apprtc.checks;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.report.Status;
import io.cosmosoftware.kite.steps.TestCheck;
import org.openqa.selenium.WebDriver;
import org.webrtc.kite.apprtc.pages.AppRTCMeetingPage;

import static io.cosmosoftware.kite.util.TestUtils.waitAround;

public class PeerConnectionCheck extends TestCheck {

  public PeerConnectionCheck(WebDriver webDriver) {
    super(webDriver);
  }
  
  @Override
  public String stepDescription() {
    return "Verify that the ICE connection state is 'connected'";
  }
  
  @Override
  protected void step() throws KiteTestException {
    final AppRTCMeetingPage appRTCMeetingPage = new AppRTCMeetingPage(webDriver, logger);
    for (int elapsedTime = 0; elapsedTime < this.checkTimeout; elapsedTime += this.checkInterval) {
      String state = appRTCMeetingPage.getICEConnectionState();
      if (state.equalsIgnoreCase("failed")) {
        throw new KiteTestException("The ICE connection's state has changed to failed", Status.FAILED);
      }
      if (state.equalsIgnoreCase("connected") || state.equalsIgnoreCase("completed")) {
        return;
      }
      waitAround(this.checkInterval);
    }
    throw new KiteTestException("Could not verify the ICE connection's state after " + this.checkTimeout + "ms", Status.FAILED);
  }
}