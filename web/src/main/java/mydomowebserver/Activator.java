package mydomowebserver;

/*
 * #%L
 * MyDomoWebServer
 * %%
 * Copyright (C) 2011 - 2013 A. de Giuli
 * %%
 * This file is part of MyDomo done by A. de Giuli (arnaud.degiuli(at)free.fr).
 * 
 *     MyDomo is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     MyDomo is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with MyDomo.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */


import java.util.Hashtable;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {

	private BundleContext bc;
	private HttpService httpService = null;
    private static final Logger logger = Logger.getLogger(Activator.class.getName());
    private ServiceTracker httpServiceTracker;

//	private ServiceTracker<LightRestService, LightRestServiceImpl> lightRestServiceTracker;
//	private LightRestService lightRestService;
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		
		System.setProperty("jetty.home.bundle", "MyDomoWebServer");
		this.bc = context;
		
		
		// register the service
		context.registerService(
				LightRestService.class.getName(), 
				new LightRestServiceImpl(), 
				new Hashtable<String, String>());
		
//		// create a tracker and track the log service
//		lightRestServiceTracker = 
//			new ServiceTracker(context, LightRestService.class.getName(), null);
//		lightRestServiceTracker.open();
//		
//		// grab the service
//		lightRestService = (LightRestService) lightRestServiceTracker.getService();
		
		
		

		logger.info("STARTING HTTP SERVICE BUNDLE");

		httpServiceTracker = new ServiceTracker(context, HttpService.class.getName(), null) {
	          @Override
	          public Object addingService(final ServiceReference serviceRef) {
	             httpService = (HttpService)super.addingService(serviceRef);
	             registerServlets();
	             try {
					httpService.registerServlet("/light", new LightServlet(), null, null);
					httpService.registerResources("/server", "", null);
				} catch (final ServletException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (final NamespaceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	             return httpService;
	          }

	          @Override
	            public void removedService(final ServiceReference ref, final Object service) {
	                if (httpService == service) {
//	                    unregisterServlets();
	                    httpService = null;
	                }
	                super.removedService(ref, service);
	            }
	        };

	        httpServiceTracker.open();
	                
	        logger.info("HTTP SERVICE BUNDLE STARTED");
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
				
		// close the service tracker
//		lightRestServiceTracker.close();
//		lightRestServiceTracker = null;
//		
//		lightRestService = null;
		
		this.httpServiceTracker.close();
	}

	private void registerServlets() {
        try {
            rawRegisterServlets();
        } catch (InterruptedException ie) {
            throw new RuntimeException(ie);
        } catch (ServletException se) {
            throw new RuntimeException(se);
        } catch (NamespaceException se) {
            throw new RuntimeException(se);
        }
    }

//    private void rawRegisterServlets() throws ServletException, NamespaceException, InterruptedException {
//        logger.info("T BUNDLE: REGISTERING SERVLETS");
//        logger.info("T BUNDLE: HTTP SERVICE = " + httpService.toString());
//
//        HttpContext hc = new HttpContext() {
//			
//			@Override
//			public boolean handleSecurity(HttpServletRequest request,
//					HttpServletResponse response) throws IOException {
//				// TODO Auto-generated method stub
//				return false;
//			}
//			
//			@Override
//			public URL getResource(String name) {
//				// TODO Auto-generated method stub
//				return null;
//			}
//			
//			@Override
//			public String getMimeType(String name) {
//				// TODO Auto-generated method stub
//				return null;
//			}
//		};
//        
//        httpService.registerServlet("/servi", new Servlet(),  new Hashtable<String, String>(), hc);
//
//        logger.info("T BUNDLE: SERVLETS REGISTERED");
//    }
//
//    private void unregisterServlets() {
//    	if (this.httpService != null) {
//    		logger.info("T BUNDLE: UNREGISTERING SERVLETS");
//    		httpService.unregister("/servi");
//    		logger.info("T BUNDLE: SERVLETS UNREGISTERED");
//    	}
//    }	
	
    private void rawRegisterServlets() throws ServletException, NamespaceException, InterruptedException {
//        logger.info("JERSEY BUNDLE: REGISTERING SERVLETS");
//        logger.info("JERSEY BUNDLE: HTTP SERVICE = " + httpService.toString());
//
//        httpService.registerServlet("/jersey-http-service", new ServletContainer(), getJerseyServletParams(), null);
//
//        sendAdminEvent();
//        logger.info("JERSEY BUNDLE: SERVLETS REGISTERED");
    }

//    private void sendAdminEvent() {
//        ServiceReference eaRef = bc.getServiceReference(EventAdmin.class.getName());
//        if (eaRef != null) {
//            EventAdmin ea = (EventAdmin) bc.getService(eaRef);
//            ea.sendEvent(new Event("jersey/test/DEPLOYED", new HashMap<String, String>() {
//
//                {
//                    put("context-path", "/");
//                }
//            }));
//            bc.ungetService(eaRef);
//        }
//    }
//
//    private voixml,\d unregisterServlets() {
//        if (this.httpService != null) {
//            logger.info("JERSEY BUNDLE: UNREGISTERING SERVLETS");
//            httpService.unregister("/jersey-http-service");
//            logger.info("JERSEY BUNDLE: SERVLETS UNREGISTERED");
//        }
//    }
//
//    @SuppressWarnings("UseOfObsoleteCollectionType")
//    private Dictionary<String, String> getJerseyServletParams() {
//        Dictionary<String, String> jerseyServletParams = new Hashtable<String, String>();
//        jerseyServletParams.put("javax.ws.rs.Application", JerseyApplication.class.getName());
//        return jerseyServletParams;
//    }
}
