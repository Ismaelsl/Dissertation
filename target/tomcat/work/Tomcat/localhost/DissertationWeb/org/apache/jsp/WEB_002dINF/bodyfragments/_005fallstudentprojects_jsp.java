/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/7.0.47
 * Generated at: 2018-01-12 13:58:07 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp.WEB_002dINF.bodyfragments;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class _005fallstudentprojects_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fform_005fform_0026_005fmodelAttribute_005fmethod_005faction;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fform_005fhidden_0026_005fvalue_005fpath_005fid_005fnobody;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fform_005fform_0026_005fmodelAttribute_005fmethod_005faction = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fform_005fhidden_0026_005fvalue_005fpath_005fid_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fform_005fform_0026_005fmodelAttribute_005fmethod_005faction.release();
    _005fjspx_005ftagPool_005fform_005fhidden_0026_005fvalue_005fpath_005fid_005fnobody.release();
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
        throws java.io.IOException, javax.servlet.ServletException {

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("\r\n");
      out.write("<h1>List of projects that you are interested</h1>\r\n");
      out.write("<script type=\"text/javascript\">\r\n");
      out.write("\r\n");
      out.write("var actualID;\r\n");
      out.write("\r\n");
      out.write("function modalPopulator(title,description,projectID,topics,compulsoryReading, lecturerName, lecturerEmail) {\r\n");
      out.write("    $(\".modal-title\").html( title );\r\n");
      out.write("    $(\"#modal-description\").html(description );\r\n");
      out.write("    $(\"#modal-topics\").html(topics );\r\n");
      out.write("    $(\"#modal-compulsoryReading\").html(compulsoryReading );\r\n");
      out.write("    $(\"#modal-lecturerName\").html(lecturerName );\r\n");
      out.write("    $(\"#modal-lecturerEmail\").html(lecturerEmail );\r\n");
      out.write("    actualID = projectID;\r\n");
      out.write("  ");
      out.write("\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("function modalPopulatorNotVisible(title,description,projectID,topics,compulsoryReading, lecturerName, lecturerEmail, user) {\r\n");
      out.write("    $(\".modal-title\").html( title );\r\n");
      out.write("    $(\"#modal-description\").html(description );\r\n");
      out.write("    $(\"#modal-topics\").html(topics );\r\n");
      out.write("    $(\"#modal-compulsoryReading\").html(compulsoryReading );\r\n");
      out.write("    $(\"#modal-lecturerName\").html(lecturerName );\r\n");
      out.write("    $(\"#modal-lecturerEmail\").html(lecturerEmail );\r\n");
      out.write("    actualID = projectID;\r\n");
      out.write("    userID = user;\r\n");
      out.write("     var userIDToRemove = document.getElementById(\"userIDRemove\");\r\n");
      out.write("     userIDToRemove.value = userID;\r\n");
      out.write("    ");
      out.write("\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("function getProjectID() { \r\n");
      out.write("    var removeRegisterInterest = document.getElementById(\"modal-removeinterest-id\");\r\n");
      out.write("    var projectRegisterInterest = document.getElementById(\"modal-makeItVisible-id\");\r\n");
      out.write("    removeRegisterInterest.value = actualID;\r\n");
      out.write("    projectRegisterInterest.value = actualID;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("function chooseMessage(listSize, finalProject){\r\n");
      out.write("\tif(listSize == 0){\r\n");
      out.write("\t\t$(\"#secondList\").html(\"You do not have any not visible projects\");\r\n");
      out.write("\t}else{\r\n");
      out.write("\t\t$(\"#secondList\").html(\"Those are the project that you make not visible, click on them if you want to make them visibles\");\r\n");
      out.write("\t}\r\n");
      out.write("\tif(finalProject){\r\n");
      out.write("\t\t$(\"#finalProject\").html(\"The final project is\");\r\n");
      out.write("\t}else{\r\n");
      out.write("\t\t$(\"#finalProject\").html(\"Student does not have a final project yet\");\r\n");
      out.write("\t}\r\n");
      out.write("}\r\n");
      out.write(" </script>\r\n");
      out.write("\r\n");
      out.write("<input type=\"hidden\" id=\"userType\" name=\"userType\" value=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${userType}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("\">\r\n");
      out.write("<body onload='chooseMessage(\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${notInterestListSize}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write('"');
      out.write(',');
      out.write('"');
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${noFinalProject}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("\")'>\r\n");
      out.write("\t<h2>\r\n");
      out.write("\t\t<div id=\"secondList\"></div>\r\n");
      out.write("\t</h2>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\t<h2>\r\n");
      out.write("\t\t<div id=\"finalProject\"></div>\r\n");
      out.write("\t</h2>\r\n");
      out.write("\t<li><a\r\n");
      out.write("\t\tonclick='modalPopulatorNotVisible(\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${finalProject.title}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write('"');
      out.write(',');
      out.write('"');
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${finalProject.description}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write('"');
      out.write(',');
      out.write('"');
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${finalProject.projectID}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("\",\r\n");
      out.write("  \"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${finalProject.topics}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write('"');
      out.write(',');
      out.write('"');
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${finalProject.compulsoryReading}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write('"');
      out.write(',');
      out.write('"');
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${finalProject.user.username}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write('"');
      out.write(',');
      out.write('"');
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${finalProject.user.email}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("\", \r\n");
      out.write("  \"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${finalProject.user.userID}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("\")'\r\n");
      out.write("\t\thref=\"#\" class=\"test\" id=\"userLoginButton\" data-toggle=\"modal\"\r\n");
      out.write("\t\tdata-target=\"#userModal\">Project title: ");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${finalProject.title}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("</a></li>\r\n");
      out.write("</body>\r\n");
      out.write("<!-- User login Modal -->\r\n");
      out.write("<div class=\"modal fade\" id=\"userModal\" tabindex=\"-1\" role=\"dialog\"\r\n");
      out.write("\taria-labelledby=\"profileModal\" aria-hidden=\"true\">\r\n");
      out.write("\t<div class=\"modal-dialog\" role=\"document\">\r\n");
      out.write("\t\t<div class=\"modal-content\">\r\n");
      out.write("\t\t\t<div class=\"modal-header\">\r\n");
      out.write("\t\t\t\t<h5 class=\"modal-title\" id=\"exampleModalLongTitle\">\r\n");
      out.write("\t\t\t\t\t<span id=\"profileTitle\"></span>\r\n");
      out.write("\t\t\t\t</h5>\r\n");
      out.write("\t\t\t\t<button type=\"button\" class=\"close\" data-dismiss=\"modal\"\r\n");
      out.write("\t\t\t\t\taria-label=\"Close\">\r\n");
      out.write("\t\t\t\t\t<span aria-hidden=\"true\">&times;</span>\r\n");
      out.write("\t\t\t\t</button>\r\n");
      out.write("\t\t\t</div>\r\n");
      out.write("\t\t\t<div class=\"modal-body\">\r\n");
      out.write("\t\t\t\t<div class=\"container-fluid\">\r\n");
      out.write("\t\t\t\t\t<div class=\"row\">\r\n");
      out.write("\t\t\t\t\t\t<b>Description:</b>\r\n");
      out.write("\t\t\t\t\t\t<div class=\"col-md-12\" id=\"modal-description\"></div>\r\n");
      out.write("\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t<div class=\"row\">\r\n");
      out.write("\t\t\t\t\t\t<b>Topics:</b>\r\n");
      out.write("\t\t\t\t\t\t<div class=\"col-md-12\" id=\"modal-topics\"></div>\r\n");
      out.write("\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t<div class=\"row\">\r\n");
      out.write("\t\t\t\t\t\t<b>Compulsory readings:</b>\r\n");
      out.write("\t\t\t\t\t\t<div class=\"col-md-12\" id=\"modal-compulsoryReading\"></div>\r\n");
      out.write("\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t<div class=\"row\">\r\n");
      out.write("\t\t\t\t\t\t<b>Lecturer</b><br /> <b>Name:</b>\r\n");
      out.write("\t\t\t\t\t\t<div class=\"col-md-12\" id=\"modal-lecturerName\"></div>\r\n");
      out.write("\t\t\t\t\t\t<b>Email:</b>\r\n");
      out.write("\t\t\t\t\t\t<div class=\"col-md-12\" id=\"modal-lecturerEmail\"></div>\r\n");
      out.write("\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t</div>\r\n");
      out.write("\t\t\t</div>\r\n");
      out.write("\t\t\t<div class=\"modal-footer\" id=\"modal-footer-removeInterest\">\r\n");
      out.write("\t\t\t\t");
      if (_jspx_meth_form_005fform_005f0(_jspx_page_context))
        return;
      out.write("\r\n");
      out.write("\t\t\t</div>\r\n");
      out.write("\t\t");
      out.write("\r\n");
      out.write("\t\t\t</div>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t</div>\r\n");
      out.write("</div>\r\n");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }

  private boolean _jspx_meth_form_005fform_005f0(javax.servlet.jsp.PageContext _jspx_page_context)
          throws java.lang.Throwable {
    javax.servlet.jsp.PageContext pageContext = _jspx_page_context;
    javax.servlet.jsp.JspWriter out = _jspx_page_context.getOut();
    //  form:form
    org.springframework.web.servlet.tags.form.FormTag _jspx_th_form_005fform_005f0 = (org.springframework.web.servlet.tags.form.FormTag) _005fjspx_005ftagPool_005fform_005fform_0026_005fmodelAttribute_005fmethod_005faction.get(org.springframework.web.servlet.tags.form.FormTag.class);
    _jspx_th_form_005fform_005f0.setPageContext(_jspx_page_context);
    _jspx_th_form_005fform_005f0.setParent(null);
    // /WEB-INF/bodyfragments/_allstudentprojects.jsp(131,4) name = method type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_form_005fform_005f0.setMethod("post");
    // /WEB-INF/bodyfragments/_allstudentprojects.jsp(131,4) name = action type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_form_005fform_005f0.setAction("removeinterest");
    // /WEB-INF/bodyfragments/_allstudentprojects.jsp(131,4) name = modelAttribute type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_form_005fform_005f0.setModelAttribute("user");
    int[] _jspx_push_body_count_form_005fform_005f0 = new int[] { 0 };
    try {
      int _jspx_eval_form_005fform_005f0 = _jspx_th_form_005fform_005f0.doStartTag();
      if (_jspx_eval_form_005fform_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\r\n");
          out.write("\t\t\t\t\t<button onclick=\"getProjectID();\" id=\"modal-removeinterest-id\"\r\n");
          out.write("\t\t\t\t\t\tname=\"projectID\" class=\"btn btn-danger\" value=\" \">Remove\r\n");
          out.write("\t\t\t\t\t\tInterest</button>\r\n");
          out.write("\t\t\t\t\t\t");
          if (_jspx_meth_form_005fhidden_005f0(_jspx_th_form_005fform_005f0, _jspx_page_context, _jspx_push_body_count_form_005fform_005f0))
            return true;
          out.write("\r\n");
          out.write("\t\t\t\t\t<button type=\"button\" class=\"btn btn-secondary\"\r\n");
          out.write("\t\t\t\t\t\tdata-dismiss=\"modal\">Close</button>\r\n");
          out.write("\t\t\t\t");
          int evalDoAfterBody = _jspx_th_form_005fform_005f0.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
      }
      if (_jspx_th_form_005fform_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
    } catch (java.lang.Throwable _jspx_exception) {
      while (_jspx_push_body_count_form_005fform_005f0[0]-- > 0)
        out = _jspx_page_context.popBody();
      _jspx_th_form_005fform_005f0.doCatch(_jspx_exception);
    } finally {
      _jspx_th_form_005fform_005f0.doFinally();
      _005fjspx_005ftagPool_005fform_005fform_0026_005fmodelAttribute_005fmethod_005faction.reuse(_jspx_th_form_005fform_005f0);
    }
    return false;
  }

  private boolean _jspx_meth_form_005fhidden_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_form_005fform_005f0, javax.servlet.jsp.PageContext _jspx_page_context, int[] _jspx_push_body_count_form_005fform_005f0)
          throws java.lang.Throwable {
    javax.servlet.jsp.PageContext pageContext = _jspx_page_context;
    javax.servlet.jsp.JspWriter out = _jspx_page_context.getOut();
    //  form:hidden
    org.springframework.web.servlet.tags.form.HiddenInputTag _jspx_th_form_005fhidden_005f0 = (org.springframework.web.servlet.tags.form.HiddenInputTag) _005fjspx_005ftagPool_005fform_005fhidden_0026_005fvalue_005fpath_005fid_005fnobody.get(org.springframework.web.servlet.tags.form.HiddenInputTag.class);
    _jspx_th_form_005fhidden_005f0.setPageContext(_jspx_page_context);
    _jspx_th_form_005fhidden_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_form_005fform_005f0);
    // /WEB-INF/bodyfragments/_allstudentprojects.jsp(135,6) name = id type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_form_005fhidden_005f0.setId("userIDRemove");
    // /WEB-INF/bodyfragments/_allstudentprojects.jsp(135,6) name = path type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_form_005fhidden_005f0.setPath("userID");
    // /WEB-INF/bodyfragments/_allstudentprojects.jsp(135,6) null
    _jspx_th_form_005fhidden_005f0.setDynamicAttribute(null, "value", "");
    int[] _jspx_push_body_count_form_005fhidden_005f0 = new int[] { 0 };
    try {
      int _jspx_eval_form_005fhidden_005f0 = _jspx_th_form_005fhidden_005f0.doStartTag();
      if (_jspx_th_form_005fhidden_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
    } catch (java.lang.Throwable _jspx_exception) {
      while (_jspx_push_body_count_form_005fhidden_005f0[0]-- > 0)
        out = _jspx_page_context.popBody();
      _jspx_th_form_005fhidden_005f0.doCatch(_jspx_exception);
    } finally {
      _jspx_th_form_005fhidden_005f0.doFinally();
      _005fjspx_005ftagPool_005fform_005fhidden_0026_005fvalue_005fpath_005fid_005fnobody.reuse(_jspx_th_form_005fhidden_005f0);
    }
    return false;
  }
}
