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

import javax.jmdns.JmDNS;

/**
 * This singleton holds the single instance of the JmDNS instance that is used to broadcast
 * Appender related information via ZeroConf.  Once referenced, a single JmDNS instance is created
 * and held.  To ensure your JVM exits cleanly you should ensure that you call the {@link #shutdown() } method
 * to broadcast the disappearance of your devices, and cleanup sockets.  (alternatively you can call the close() 
 * method on the JmDNS instead, totally up to you...)
 * 
 * See http://jmdns.sf.net for more information about JmDNS and ZeroConf.
 * 
 * @author psmith
 *
 */
public class Zeroconf4log4j {

    private static final JmDNS instance;

    static {
        try {
            instance = new JmDNS();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize JmDNS");
        }
    }

    /**
     * Returns the current instance of the JmDNS being used by log4j.
     * 
     * @throws IllegalStateException if JmDNS was not correctly initialized.
     * 
     * @return
     */
    public static JmDNS getInstance() {
        checkState();
        return instance;
    }

    private static void checkState() {
        if (instance == null) {
            throw new IllegalStateException(
                    "JmDNS did not initialize correctly");
        }
    }
    
    /**
     * Ensures JmDNS cleanly broadcasts 'goodbye' and closes any sockets, and (more imporantly)
     * ensures some Threads exit so your JVM can exit.
     *
     */
    public static void shutdown() {
        checkState();
        instance.close();
    }
}
