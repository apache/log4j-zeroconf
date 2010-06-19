/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.log4j.net;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * 
 * A test bed class to configure and launch a ZeroConfSocketHubAppender and stream
 * some LoggingEvents to it so that one can test Chainsaw
 * 
 * @author psmith
 *
 */
public class ZeroConfSocketHubAppenderTestBed {

    public static void main(String[] args) throws Exception {
        ZeroConfSocketHubAppender appender = new ZeroConfSocketHubAppender();
        appender.setName("foo");
        appender.activateOptions();
        Logger LOG = LogManager.getRootLogger();
        LOG.addAppender(appender);
        
        while(true) {
            LOG.info("TestBedEvent: " + System.currentTimeMillis());
            Thread.sleep(250);
        }
        
        
    }
}
