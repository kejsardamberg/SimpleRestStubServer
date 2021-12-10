class ResponseRecorder {
    constructor(){
        this.clientRequestHeaders = [];
        var recorder = document.createElement("div");
        recorder.classList.add("recordClient");

        var heading = document.createElement("h2");
        heading.innerHTML = "HTTP record client";
        recorder.appendChild(heading);

        var requestDiv = document.createElement("div");

        var requestheading = document.createElement("h3");
        requestheading.innerHTML = "Request";
        requestDiv.appendChild(requestheading);

        requestDiv.appendChild(new FieldWithLabel("URL:", "recordendpoint"), "http://");
        requestDiv.appendChild(new FieldWithLabel("Method:", "recordmethod", "GET"));
        var requestBodyArea = document.createElement("textarea");
        requestBodyArea.id = "recordbody";
        requestDiv.appendChild(requestBodyArea);

        var headersDiv = document.createElement("div");
        headersDiv.innerHTML = "Headers:";
        requestDiv.appendChild(headersDiv);

        var headersList = document.createElement("span");
        headersList.id = "recordheaders";
        requestDiv.appendChild(headersList);
        requestDiv.appendChild(new FieldWithLabel("Header name:", "recordresponseheadername", ""));
        requestDiv.appendChild(new FieldWithLabel("Header value:", "recordresponseheadervalue", ""));
        
        var addHeaderBtn = document.createElement("button");
        addHeaderBtn.innerHTML = "Add header";
        addHeaderBtn.onclick = this.addRequestHeader();
        addHeaderBtn.id = "recordaddheaderbutton";
        requestDiv.appendChild(addHeaderBtn);

        var sendRequestBtn = document.createElement("button");
        sendRequestBtn.innerHTML = "Send";
        sendRequestBtn.onclick = this.sendrecordrequest();
        sendRequestBtn.id = "recordsendbutton";
        requestDiv.appendChild(sendRequestBtn);

        recorder.appendChild(requestDiv);

        var responseDiv = document.createElement("div");
        var respHeading = document.createElement("h3");
        respHeading.innerHTML = "Response";

        recorder.appendChild(responseDiv);
        return recorder;
    }

    addRequestHeader(){
        if(!validateFieldIsNotDefault("clientresponseheadername") || !validateFieldIsNotDefault("clientresponseheadervalue"))return;
        var header = {};
        header.name = document.getElementById('clientresponseheadername').value;
        header.value = document.getElementById('clientresponseheadervalue').value;
        this.clientRequestHeaders.push(header);
        document.getElementById('clientheaders').innerHTML += header.name + ": " + header.value + "<br>";
    }

    sendrecordrequest() {
        if(!validateFieldIsNotDefault("recordendpoint")) return;
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {
            if (this.readyState == 4) {
                document.getElementById('saverecordedbutton').disabled = false;
                document.getElementById('clearrecordedresponsebutton').disabled = false;
                document.getElementById('recordresponsebodyvalue').innerHTML = textToHtml(this.responseText);
                document.getElementById('recordstatuscodevalue').innerHTML = this.status;
            }
        };
        xhttp.open(document.getElementById('recordmethod').value, "/sgsdfjfdsfgf", false);
        xhttp.setRequestHeader('ServiceVirtualizationForward', document.getElementById('recordendpoint').value);
        xhttp.send(document.getElementById('recordbody').value);
    }

    clearreceivedrecordresponse() {
        document.getElementById('recordresponseheaders').innerHTML = '';
        document.getElementById('recordstatuscodevalue').innerHTML = '';
        document.getElementById('recordresponsebodyvalue').innerHTML = '';
        document.getElementById('saverecordedbutton').disabled = true;
        document.getElementById('clearrecordedresponsebutton').disabled = true;
    }

}

class HttpResponse {
    constructor(statusCode, body, headers){

    }
}

class HttpRequest {
    constructor(url, method, body, headers){
        this.url = url;
        this.method = method;
        this.headers = headers;
        this.body = body;
    }

    send(){
        var xhttp = new XMLHttpRequest();
        xhttp.open(this.method, this.url, false);
        xhttp.send(this.body);
        return new HttpResponse(xhttp.status, xhttp.responseText, xhttp.getAllResponseHeaders());
    }
}

class FieldWithLabel{
    constructor(label, id, defaultText){
        var container = document.createElement("div");
        container.classList.add(id);
        container.classList.add("textFieldContainer");
        var labelElement = document.createElement("label");
        labelElement.setAttribute("for", id);
        labelElement.classList.add("textFieldLabel");
        labelElement.innerHTML = label;
        container.appendChild(labelElement);
        var field = document.createElement("input");
        field.type = "text";
        field.id = id;
        field.value = defaultText;
        field.setAttribute("originalValue", defaultText);
        container.appendChild(field);
        return container;
    }
}

class HeaderDefinitionGui {
    constructor(){
        this.clientRequestHeaders = [];
        var headersDiv = document.createElement("div");
        this.headersList = document.createElement("table");
        headersList.classList.add = "recordheaders";
        requestDiv.appendChild(headersList);
        this.headerName = new FieldWithLabel("Header name:", "recordresponseheadername", "");
        requestDiv.appendChild(this.headerName);
        this.headerValue = new FieldWithLabel("Header value:", "recordresponseheadervalue", "");
        requestDiv.appendChild(this.headerValue);
        
        var addHeaderBtn = document.createElement("button");
        addHeaderBtn.innerHTML = "Add header";
        addHeaderBtn.onclick = this.addRequestHeader();
        addHeaderBtn.id = "recordaddheaderbutton";
        requestDiv.appendChild(addHeaderBtn);
        return requestDiv;
    }

    get headers(){
        return this.clientRequestHeaders;
    }

    addRequestHeader(){
        if(!validateFieldIsNotDefault("recordresponseheadername") || !validateFieldIsNotDefault("recordresponseheadervalue"))return;
        var header = {};
        header.name = this.headerName.value;
        header.value = this.headerValue.value;
        this.clientRequestHeaders.push(header);
        this.headersList.innerHTML = "";
        for(var i = 0; i < this.clientRequestHeaders.length; i++){
            var registeredHeaderDiv = document.createElement("tr");
            registeredHeaderDiv.setId("registeredHeaderNr" + i);
            var registeredHeaderName = document.createElement("td");
            registeredHeaderName.innerHTML = this.clientRequestHeaders[i].name;
            registeredHeaderDiv.appendChild(registeredHeaderName);
            var registeredHeaderValue = document.createElement("td");
            registeredHeaderValue.innerHTML = this.clientRequestHeaders[i].value;
            registeredHeaderDiv.appendChild(registeredHeaderValue);
            var buttonTd = document.createElement("td");
            var deleteButton = document.createElement("button");
            deleteButton.onclick = this.clientRequestHeaders.pop(this.clientRequestHeaders[i]);
            buttonTd.appendChild(deleteButton);
            registeredHeaderDiv.appendChild(buttonTd);
            this.headersList.appendChild(registeredHeaderDiv);
        }
        this.headersList.innerHTML += header.name + ": " + header.value + "<br>";
    }


}

class RequestDefinitionGui {
    constructor(){
        var requestDiv = document.createElement("div");

        var requestheading = document.createElement("h3");
        requestheading.innerHTML = "Request";
        requestDiv.appendChild(requestheading);

        requestDiv.appendChild(new FieldWithLabel("URL:", "recordendpoint"), "http://");
        requestDiv.appendChild(new FieldWithLabel("Method:", "recordmethod", "GET"));
        var requestBodyArea = document.createElement("textarea");
        requestBodyArea.id = "recordbody";
        requestDiv.appendChild(requestBodyArea);

        var headersDiv = document.createElement("div");
        headersDiv.innerHTML = "Headers:";
        this.headersGui = new HeaderDefinitionGui();
        requestDiv.appendChild(this.headersGui);

        var sendRequestBtn = document.createElement("button");
        sendRequestBtn.innerHTML = "Send";
        sendRequestBtn.onclick = this.sendrecordrequest();
        sendRequestBtn.id = "recordsendbutton";
        requestDiv.appendChild(sendRequestBtn);
    }
}
