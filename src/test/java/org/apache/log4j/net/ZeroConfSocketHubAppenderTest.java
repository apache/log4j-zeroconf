package org.apache.log4j.net;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

import junit.framework.TestCase;

/**
 * Some test methods to validate that the ZeroConf stuff works as expected/advertised
 * 
 * @author psmith
 */
public class ZeroConfSocketHubAppenderTest extends TestCase {

    private final class TestServiceListener implements ServiceListener {
        final ModifiableBoolean addedFlag = new ModifiableBoolean();
        final ModifiableBoolean removedFlag = new ModifiableBoolean();
        private ServiceInfo lastInfo;
        private ServiceEvent lastEvent;

        public void serviceAdded(ServiceEvent event) {
            addedFlag.setValue(true);
            lastEvent = event;
        }

        public void serviceRemoved(ServiceEvent event) {
            removedFlag.setValue(true);
            lastEvent = event;
        }

        public void serviceResolved(ServiceEvent event) {
            lastInfo = event.getInfo();
        }
    }

    private static final int DEFAULT_TIMEOUT_FOR_ZEROCONF_EVENTS_TO_APPEAR = 2000;

    JmDNS jmdns;

    protected void setUp() throws Exception {
        super.setUp();
        jmdns = (JmDNS) ZeroConfSupport.getJMDNSInstance();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        jmdns.close();
    }

    /**
     * This does a simple test, as a test harness, to make sure the Appender can be created and that
     * it can shutdown appropriately. in older versions of JmDNS a non-daemon thread could hold the
     * JVM open preventing it from shutting down.
     * 
     * @see com.strangeberry.jmdns.tools.Main for a ZeroConf Network browser in Swing allowing you
     *      to see the broadcasts
     * @throws Exception
     */
    public void testSimpleTest() throws Exception {

        TestServiceListener testServiceListener = new TestServiceListener();
        jmdns.addServiceListener(
                ZeroConfSocketHubAppender.DEFAULT_ZEROCONF_ZONE,
                testServiceListener);
        ZeroConfSocketHubAppender appender = new ZeroConfSocketHubAppender();
        appender.setName("SimpleTest");
        appender.activateOptions();

        Thread.sleep(DEFAULT_TIMEOUT_FOR_ZEROCONF_EVENTS_TO_APPEAR);

        assertTrue("Should have detected the addition",
                testServiceListener.addedFlag.isSet());

        appender.close();

        Thread.sleep(DEFAULT_TIMEOUT_FOR_ZEROCONF_EVENTS_TO_APPEAR);

    }

    public void testRandomPortWorksOk() throws Exception {
        
        TestServiceListener testServiceListener = new TestServiceListener();
        jmdns.addServiceListener(
                ZeroConfSocketHubAppender.DEFAULT_ZEROCONF_ZONE,
                testServiceListener);

        
        ZeroConfSocketHubAppender appender = new ZeroConfSocketHubAppender();
        appender.setPort(0);
        appender.setName("RandomPortTest");
        appender.activateOptions();
        assertTrue("Port should have been automatically chosen", appender
                .getActualPortUsed() != 0);
        assertTrue("Should have detected the addition",
                testServiceListener.addedFlag.isSet());

        ServiceEvent lastEvent = testServiceListener.lastEvent;
        jmdns.requestServiceInfo(lastEvent.getType(), lastEvent.getName());
        assertEquals(
                "The JmDNS port ServiceInfo should have matched what we expect to broadcast from the appender",
                testServiceListener.lastInfo.getPort(), appender
                .getActualPortUsed());
        
        appender.close();

    }
}
