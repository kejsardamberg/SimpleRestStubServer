# SimpleRestStubServer
Actual home page, with __DOWNLOAD__ and screenshots, here: http://damberg.one/alster/work/reststubserver/stubserver.html

## Introduction
A system rarely stands on its own. It almost always have integrations to other systems. When testing a system of your own it's easy to use mocks while running unit tests, but when rising to system testing level you need the external systems to respond. A problem with this is that you don't know if the external systems are in sync with you with their functionality, and another challenge is that it's ofter cumbersome and hard to prepare data into the external systems so you can test your own system properly. This is of course extra hard for negative testing, with rare error conditions. 

![Stubbing explained](http://damberg.one/alster/work/reststubserver/stub.JPG)

This utility is meant to be able to substitute any REST integration for your system.

## What it is
This is a java utility to be ran as stand-alone. You point your system under test to send its HTTP REST requests to the IP address of this utility rather than the external system it should have used. Then you prepare the utility with what to answer.

### Benefits
* No installation required (apart from Java JRE)
* Can run on machines without desktop GUI/Windows manager since administered from web interface
* Versatile response configuration from API as well as from GUI

## Getting started
### Steps:
1. Download the utility and place it somewhere on your system. It's beneficial if it's in a folder where you have permanent write access to the file system since percistance of stub responses is made to the same folder. Actual home page, with DOWNLOAD and screenshots, here: http://damberg.one/alster/work/reststubserver/stubserver.html
1. Start the server
1. Open your web browser to the given admin URL and start configure the stub responses

### Starting the server
You start the executable jar that you downloaded with the following command:

     java.exe -jar Stub_v1.1.2.jar
      
Optionally you may enter a port of your choice as argument, like so:

    java.exe -jar Stub_v1.1.2.jar 8023
        
You notice the server is running since it is producing a text like this:

     Service Virtualization REST server
     ==================================
     Admin interface at http://192.168.1.3:8089/admin
     API at http://192.168.1.3:8089/api
     
     Server started.
     Listening for connections on port : 8023 ...
        
### Configuring responses
Now point your web browser to the given admin URL to configure the utility behaviour.

First you'll see a list of registered responses. 

![Screenshot](http://damberg.one/alster/work/reststubserver/list.JPG?)

To add more responses, just click the Add... button.

![Screenshot](http://damberg.one/alster/work/reststubserver/add1.jpg)

You may customize both the response and parameters for when to use exactly this response. The filter part is dynamic and its fields are dependent on the type of filter selected.

You may also choose that the response body should be read from file by the stub server at runtime. To do this, switch the Resonse body content type to 'file': 

![Screenshot](http://damberg.one/alster/work/reststubserver/file.JPG)

This file path is of-course given in a stub server perspective, and need to be readable from the stub server.

When you start adding HTTP headers or filters, they get listed in their respective section. 

![Screenshot](http://damberg.one/alster/work/reststubserver/add.jpg)

For the response you may choose status code (default is 200), body context (default is blank), and HTTP headers (default are no extra headers).

As filters for when to apply which response you can combine a range of filters:

![Screenshot](http://damberg.one/alster/work/reststubserver/filters.JPG)

 When you change filter type, a short description get visible to explain the effects of this filter.
 
## API
The API is accessed by endpoint '/api' to this server. There are a few tricky ways of using this:

### POST anything to the API to set next response
POSTing something to the API that doesn't fulfill the object structure of a prepared statement will make the API interprete the POSTed body content as the next body content to supply as a prepared response body.

Since the filter used for this POSTed body will be NextResponse it will only be sent once (and not sent at all if end-point '/admin' or '/api' are requested).

As an example; if the following POST was performed, the next HTTP request to the server would result in a response with the body content 'username=damberg&password=topsecret'.

     curl -X POST -d "username=damberg&password=topsecret" http://localhost:8089/api

### GET
A get request to the '/api' end-point returns all registered prepared responses.

### DELETE
A DELETE request should have a JSON body stating what PreparedHttpResponse id to remove.

Body example:

    {"id":"ae037e4c-3132-4a9b-a2fb-70e30160ff42"}
    
Or, with curl:

    curl -X DELETE -d "{'id':'ae037e4c-3132-4a9b-a2fb-70e30160ff42'}" http://localhost:8089/api
    
### POSTing full PreparedHttpResponses
This web page for example uses a more complex structure for POSTing new PreparedHttpResponses.

For details on how to use this file, a Swagger file is included in the resources of this server jar file (open as a zip file and browse to the resources). Or you can download it from here: http://damberg.one/alster/work/reststubserver/stubserver.html

## Notes
This utility is built with extendability in mind. It should(tm) be able to incorporate other technologies, apart from the HTTP protocol, as well.

### Terminology
#### Registered prepared responses
When registering new prepared responses they are combined with a set of filters for when to respond with that particular prepared response.

#### Prepared responses
This tool uses prepared responses. That is you tell this server what to respond when asked to respond.

Responses consist of headers, HTTP status code, and a body.

#### Body
The body is probably the most important part of any prepared response. It's generally the common way of sending data between systems.

There are two types of body content references implemented: One is by direct input. This means that the given body content string will be handed out as body of the response. The other is by file reference. This enables to dynamically produce a file with the content being handed as response upon a request. The file is read at runtime by the server utility, so it must be readable from the server (given firewalls, file system user rights, path format and so forth).

The HTTP response body of this utility is a pure bitstream that is served as a string with no stated character encoding.

#### Headers
The HTTP headers of a response are simplified to only have one value per header.

#### Status code
Any HTTP response has a status code. Unless stated explicitly the status code is 200 (OK). However, the status code may be set to whatever number you want it to be, for example 404.
Filters/triggers
To enable having multiple different prepared responses at the same time this utility uses a filtering mechanism. These filters are applied on the incoming request to the server. If all filters for a prepared response matches, that response is sent back as answer to the request,
If no filters are given, the server responds with the prepared response unless internal paths ('/api', '/admin') are encountered in the incoming request.
If several prepared responses matches the filters the first applicable one is sent.

The following filters are implemented:
* __Request body contains string__ (if the request body contains the given string, the response is sent)
* __Request body regex match__ (if the request body matches the given pattern, the response is sent)
* __URL End-point equals__ (if the request URL path equals the given string, the response is sent)
* __Header exist__ (if the request has a header with a name that equals the given string, the response is sent)
* __Header value__ (if the request body contains the given header, equal in both name and value, the response is sent)
* __Origin URL host equals_ (if the request Origin header matches, the response is sent)
* __HTTP verb/method equals__ (case-insensitive match towards the request HTTP method, like GET, POST, PUT, DELETE)

## List of future improvement ideas
* Include a HTTP client to enable a proxy mode for recording (and to test your own filter mechaisms while creating new prepared responses).
* Expand the body options to include possiblities to modify the prepared response body at runtime, for timestamps and more.
* Prettify the XML or JSON data.
* Finish flaky HTTP Header management for prepared responses.
* Test with OpenJRE.
* Fix bug so you shouldn't have to refresh page after DELETE
* Create more tests for ease of changes.

