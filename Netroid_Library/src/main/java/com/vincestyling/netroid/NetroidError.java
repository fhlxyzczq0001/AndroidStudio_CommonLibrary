/*
 * Copyright (C) 2015 Vince Styling
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
package com.vincestyling.netroid;

/**
 * Exception style class encapsulating Netroid errors
 */
public class NetroidError extends Exception {
    public final NetworkResponse networkResponse;
    public NetroidError() {
        networkResponse = null;
    }

    public NetroidError(NetworkResponse response) {
        networkResponse = response;
    }

    public NetroidError(String exceptionMessage) {
        super(exceptionMessage);
        networkResponse = null;
    }
    public NetroidError(String exceptionMessage, Throwable reason) {
        super(exceptionMessage, reason);
        networkResponse = null;
    }

    public NetroidError(Throwable cause) {
        super(cause);
        networkResponse = null;
    }
}
