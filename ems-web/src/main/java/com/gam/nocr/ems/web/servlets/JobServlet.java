//package com.gam.nocr.ems.web.servlets;
//
//import com.gam.commons.core.BaseLog;
//import com.gam.commons.scheduler.SchedulerService;
//import com.gam.nocr.ems.config.WebExceptionCode;
//import org.quartz.Job;
//import org.quartz.JobDetail;
//import org.quartz.JobExecutionContext;
//import org.quartz.SchedulerException;
//import org.slf4j.Logger;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.Date;
//import java.util.List;
//
///**
// * @author: Haeri (haeri@gamelectronics.com)
// */
//@WebServlet(name = "JobServlet", urlPatterns = "/JobServlet")
//public class JobServlet extends HttpServlet {
//
//    private static final Logger logger = BaseLog.getLogger(JobServlet.class);
//
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        processRequest(request, response);
//    }
//
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        processRequest(request, response);
//    }
//
//    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        PrintWriter out = response.getWriter();
//        try {
//            if (request.getParameter("method") == null) {
//                out.println("No method assigned to servlet");
//            } else if (request.getParameter("method").equals("loadJobs")) {
//                loadJobs(out);
//            } else if (request.getParameter("method").equals("pauseJob")) {
//                pauseJob(request, out);
//            } else if (request.getParameter("method").equals("resumeJob")) {
//                resumeJob(request, out);
//            } else if (request.getParameter("method").equals("runJob")) {
//                runJob(request, out);
//            } else {
//                out.println("Method '" + request.getParameter("method") + "' not found");
//            }
//
//        } finally {
//            out.close();
//        }
//    }
//
//    private void runJob(HttpServletRequest request, PrintWriter out) {
//        SchedulerService schedulerService = null;
//
//        try {
//            String keyName = request.getParameter("keyName");
//            schedulerService = (SchedulerService) getServletContext().getAttribute("scheduler");
//            List<JobDetail> jobDetails = schedulerService.getJobsList(schedulerService.getScheduler());
//            for (JobDetail job : jobDetails) {
//                if (job.getKey().getName().equals(keyName)) {
//                    try {
//                        Job myJob = job.getJobClass().newInstance();
//                        myJob.execute(null);
//
////						 List<Trigger> triggers = (List<Trigger>) schedulerService.getScheduler().getTriggersOfJob(job.getKey());
////						Trigger trigger = TriggerBuilder.newTrigger()
////								.withIdentity(triggerKey("myTrigger", "myTriggerGroup"))
////								.withSchedule(simpleSchedule().withRepeatCount(1).withIntervalInSeconds(5))
////								.startAt(new Date())
////								.build();
//
//
////						schedulerService.attachSimpleTrigger(schedulerService.getScheduler(), job, triggers.get(0).getKey().getName(),triggers.get(0).getKey().getGroup(),new Date());
//                    } catch (Exception e) {
//                        logger.error(WebExceptionCode.GLB_ERR_MSG, e);
//                        return;
//                    }
//                    out.println(keyName + " was run at " + new Date());
//                    return;
//                }
//            }
//            out.println("Could not resume job: " + keyName);
//        } catch (Exception e) {
//            logger.error(WebExceptionCode.GLB_ERR_MSG, e);
//        }
//    }
//
//
//    private void loadJobs(PrintWriter out) {
//        SchedulerService schedulerService = null;
//        try {
//            schedulerService = (SchedulerService) getServletContext().getAttribute("scheduler");
//
//            List<JobDetail> jobDetails = schedulerService.getJobsList(schedulerService.getScheduler());
//            List<JobExecutionContext> contexts = null;
//            try {
//                contexts = schedulerService.getScheduler().getCurrentlyExecutingJobs();
//            } catch (SchedulerException e) {
//                out.println("<tr> <td>");
//                logger.error(WebExceptionCode.GLB_ERR_MSG, e);
//                out.println("</td> </tr>");
//                return;
//            }
//            for (JobDetail job : jobDetails) {
//                out.println("<tr>");
//                out.println("<td>" + job.getKey().getName() + "</td>");
//                out.println("<td>" + job.getDescription() + "</td>");
//                if (contexts == null) {
//                    out.println("<td>Error: List of currently executing jobs not found</td>");
//                } else {
//                    String executing = "NO";
//                    for (JobExecutionContext context : contexts) {
//                        if (job.getKey().equals(context.getJobDetail().getKey())) {
//                            executing = "YES";
//                            break;
//                        }
//                    }
//                    out.println("<td>" + executing + "</td>");
//                }
//                out.println("<td><button type=\"button\" onclick=\"pauseJob('" + job.getKey().getName() + "')\">Pause</button></td>");
//                out.println("<td><button type=\"button\" onclick=\"resumeJob('" + job.getKey().getName() + "')\">Resume</button></td>");
//                out.println("<td><button type=\"button\" onclick=\"runJob('" + job.getKey().getName() + "')\">Run</button></td>");
//                out.println("</tr>");
//            }
//        } catch (Exception e) {
//            out.println("<tr> <td>");
//            logger.error(WebExceptionCode.GLB_ERR_MSG, e);
//            out.println("</td> </tr>");
//        }
//    }
//
//    private void pauseJob(HttpServletRequest request, PrintWriter out) {
//        SchedulerService schedulerService = null;
//        try {
//            schedulerService = (SchedulerService) getServletContext().getAttribute("scheduler");
//        } catch (Exception e) {
//            logger.error(WebExceptionCode.GLB_ERR_MSG, e);
//            return;
//        }
//        List<JobDetail> jobDetails = schedulerService.getJobsList(schedulerService.getScheduler());
//        String keyName = request.getParameter("keyName");
//        for (JobDetail job : jobDetails) {
//            if (job.getKey().getName().equals(keyName)) {
//                try {
//                    schedulerService.pauseJob(schedulerService.getScheduler(), job.getKey());
//                } catch (Exception e) {
//                    logger.error(WebExceptionCode.GLB_ERR_MSG, e);
//                    return;
//                }
//                out.println(keyName + " was paused at " + new Date());
//                return;
//            }
//        }
//        out.println("Could not pause job: " + keyName);
//    }
//
//    private void resumeJob(HttpServletRequest request, PrintWriter out) {
//        SchedulerService schedulerService = null;
//        try {
//            schedulerService = (SchedulerService) getServletContext().getAttribute("scheduler");
//        } catch (Exception e) {
//            logger.error(WebExceptionCode.GLB_ERR_MSG, e);
//            return;
//        }
//        List<JobDetail> jobDetails = schedulerService.getJobsList(schedulerService.getScheduler());
//        String keyName = request.getParameter("keyName");
//        for (JobDetail job : jobDetails) {
//            if (job.getKey().getName().equals(keyName)) {
//                try {
//                    schedulerService.resumeJob(schedulerService.getScheduler(), job.getKey());
//                } catch (Exception e) {
//                    logger.error(WebExceptionCode.GLB_ERR_MSG, e);
//                    return;
//                }
//                out.println(keyName + " was resumed at " + new Date());
//                return;
//            }
//        }
//        out.println("Could not resume job: " + keyName);
//    }
//}
