# aceoffix7-springboot3-simple

**Latest Version：7.3.1.1**

### 1. Introduction

The aceoffix7-springboot3-simple project demonstrates how to use the Aceoffix 7.0 product with the Java. This project
showcases the simplest way to open, edit, and save Word files on web pages.

### 2. Project environmental prerequisites

IntelliJ IDEA, JDK version 17 or higher.

### 3. Steps for running the project

- Create a new aceoffix system folder on the current server computer, for example: D:/aceoffix. (This folder will be
  used to store the authorization file “license.lic” generated after Aceoffix registration and the Aceoffix client
  program.)

- Use "git clone" or directly download the project's compressed package to your local machine and then decompress it.

- Download the Aceoffix client program.

  [aceclientsetup_7.x.x.x.exe](https://github.com/aceoffix/aceoffix7-client/releases/)

- Copy the Aceoffix client program downloaded in the previous step to the newly created Aceoffix folder.

- Run this project to see the sample effect.

### 4. Trial license key

- Aceoffix Standard V7.0 is 4ZDGS-FDZDK-WK18-YSJET

- Aceoffix Enterprise V7.0 is QA2JS-8C0PT-IKKJ-VTCC6

- Aceoffix Ultimate V7.0 is 9GRX9-VFFED-6NSN-ACVR1

### 5. How to integrate AceoffixV7 into your web project

- Download the Aceoffix client program.

  [aceclientsetup_7.x.x.x.exe](https://github.com/aceoffix/aceoffix7-client/releases/)

- Copy the Aceoffix client program downloaded in the previous step to the newly created Aceoffix folder.

- Introduce the Aceoffix dependency in the `pom.xml` file of your project using the following code. The `aceoffix.jar`
  has been published to the [Maven Central Repository](https://central.sonatype.com/artifact/com.acesoftcorp/aceoffix).It is recommended to use the latest version.
  
  use the following `pom.xml` configuration.
  
  ```xml
  <dependency>
       <groupId>com.acesoftcorp</groupId>
        <artifactId>aceoffix</artifactId>
        <version>7.3.1.1</version>
  </dependency>
  ```
  
- Configure the following code in the `Application` class, which is the startup class of your project.

```java
   @Bean
    public ServletRegistrationBean aceoffixRegistrationBean() {
        com.acesoftcorp.aceoffix.aceserver.Server aceserver = new com.acesoftcorp.aceoffix.aceserver.Server();
        //Set the directory where the license.lic file is stored after successful registration of Aceoffix.
        aceserver.setSysPath(aceSysPath);
        ServletRegistrationBean srb = new ServletRegistrationBean(aceserver);
        srb.addUrlMappings("/server.ace");
        srb.addUrlMappings("/aceclient");
        srb.addUrlMappings("/aceoffix.js");
        return srb;
    }
    /**
     *Enable WebSocket configuration for Aceoffix. Required for Aceoffix v7.3.1.1 and above.
     *@return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        ServerEndpointExporter exporter = new ServerEndpointExporter();
        exporter.setAnnotatedEndpointClasses(
                com.acesoftcorp.aceoffix.aceserver.WServer.class
        );
        return exporter;
    }


    /**
     *Aceoffix acewserver configure cross-domain. Required for Aceoffix v7.3.1.1 and above.
     *
     * @return
     */
    @Bean
    public ServletListenerRegistrationBean acewContextListener() {
        return new ServletListenerRegistrationBean<>(new AceWContextListener());
    }

    @Bean
    public ServletContextInitializer aceoffixContextParams() {
        /*
         * Aceoffix acewserver Cross-Domain Security Configuration:
         * 1. Using "*" is not recommended in production. It is advisable to explicitly specify allowed domain(s)/IP(s).
         * 2. Format: Multiple addresses should be separated by commas, e.g., "domain1,domain2,ip".
         *    Note: Local development environment addresses (localhost, 127.0.0.1) must also be included in this configuration.
         * 3. Examples:
         *    - For Frontend/Backend Separation: "frontend-domain-address, frontend-ip-address, backend-address"
         *      (e.g., "ui.example.com,192.168.1.1,localhost")
         *    - For Monolithic Multi-Entry Applications: "domain, ip"
         *      (e.g., "www.oa.com,192.168.1.100")
         */
        return servletContext ->
                servletContext.setInitParameter("acewserver-allowedOrigins", "*");
    }
```

- Add the following code to the parent page of the page which you want to edit Office document. Write the following code
  in the <head> tag..

```javascript
<script type="text/javascript" src="aceoffix.js"></script>
```

> Note: The path of aceoffix.js is relative to the root of your website.

Write the following link to pop up an Acebrowser window for editing an Office document. Let's assume that clicking a
link in index.html pops up Acebrowser and the page that contains the Aceoffix control is "Word.html".Then we write the
index.html page.

```html
 <a href="javascript:AceBrowser.openWindow('/doc/openFile',  'width=1150px;height=900px;');">Open Word File</a>
```

- Then, write the following server code in "controllers/DocumentController.java".

```Java
@RequestMapping(value = "/openFile")
public ModelAndView openFile(HttpServletRequest request) {
    AceoffixCtrl aceCtrl = new AceoffixCtrl(request);
    aceCtrl.webOpen("/doc/editword.docx" , OpenModeType.docNormalEdit, "Luna");
    request.setAttribute("aceoffix", aceCtrl.getHtml());
    ModelAndView mv = new ModelAndView("Word");
    return mv;
}
```

-
Add a new function called Save in  "controllers/DocumentController.java"  if your user wants to save document.

```java
@RequestMapping("/saveFile")
public void saveFile(HttpServletRequest request, HttpServletResponse response) {
    FileSaver fs = new FileSaver(request, response);
    fs.saveToFile(dir + fs.getFileName());
    fs.close();
}
```

- Please continue with the front-end code for the "templates/Word.html".

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Word</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
</head>
<body>
<script type="text/javascript">
    function SaveDoc() {
        aceoffixctrl.SaveFilePage = "/doc/saveFile";
        aceoffixctrl.WebSave();
    }

    function OnAceoffixCtrlInit() {
        aceoffixctrl.AddCustomToolButton("Save", "SaveDoc()", 1);
    }
</script>
<div style=" width:auto; height:98vh;" th:utext="${aceoffix}">
</div>
</body>
</html>
```

- When publish the project , follow the prompts to install the Aceoffix V7 client. Once the registration dialog box
  appears, please enter the license key of Aceoffix V7 to complete the registration.

