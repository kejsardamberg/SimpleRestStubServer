class HTTPClientOverlay {
    constructor(){
        this.clientheaders = [];

        this.rootElement = document.createElement('div');

        var heading = document.createElement('h2');
        heading.innerHTML = "HTTP test client";
        this.rootElement.appendChild(heading);

        var requestDiv = document.createElement('div');

        var endpointLabel = document.createElement('label');
        endpointLabel.for = "clientendpoint";
        endpointLabel.innerHTML = "End-point:";
        requestDiv.appendChild(endpointLabel);
        var endpointField = document.createElement('input');
        endpointField.type = "text";
        endpointField.id = "clientendpoint";
        endpointField.value = '/test';
        requestDiv.appendChild(endpointField);

        var clientmethodLabel = document.createElement('label');
        clientmethodLabel.for = "clientmethod";
        clientmethodLabel.innerHTML = "Method:";
        requestDiv.appendChild(clientmethodLabel);
        var clientmethodField = document.createElement('input');
        clientmethodField.type = "text";
        clientmethodField.value = 'GET';
        clientmethodField.id = "clientmethod";
        requestDiv.appendChild(clientmethodField);

        requestDiv.appendChild(document.createElement('br'));

        var bodyLabel = document.createElement('label');
        bodyLabel.innerHTML = "Body:";
        bodyLabel.for = "clientbody";
        requestDiv.appendChild(bodyLabel);
        var bodyField = document.createElement('textarea')
        bodyField.id = "clientbody";
        requestDiv.appendChild(bodyField);

        var headersHeading = document.createElement("div");
        headersHeading.innerHTML = "Headers:";
        requestDiv.appendChild(headersHeading);

        var clientHeadersSpan = document.createElement('span');
        clientHeadersSpan.id = "clientheaders";
        requestDiv.appendChild(clientHeadersSpan);

        var clientresponseheadernameLabel = document.createElement('label');
        clientresponseheadernameLabel.for = "clientresponseheadername";
        clientresponseheadernameLabel.innerHTML = "Header name";
        requestDiv.appendChild(clientresponseheadernameLabel);
        var clientresponseheadernameField = document.createElement('input');
        clientresponseheadernameField.type = "text";
        clientresponseheadernameField.setAttribute('value', '');
        clientresponseheadernameField.id = "clientresponseheadername";
        requestDiv.appendChild(clientresponseheadernameField);

        requestDiv.appendChild(document.createElement('br'));

        var clientresponseheadervalueLabel = document.createElement('label');
        clientresponseheadervalueLabel.for = "clientresponseheadervalue";
        clientresponseheadervalueLabel.innerHTML = "Header value";
        requestDiv.appendChild(clientresponseheadervalueLabel);
        var clientresponseheadervalueField = document.createElement('input');
        clientresponseheadervalueField.type = "text";
        clientresponseheadervalueField.setAttribute('value', '');
        clientresponseheadervalueField.id = "clientresponseheadervalue";
        requestDiv.appendChild(clientresponseheadervalueField);

        requestDiv.appendChild(document.createElement('br'));
        
        var addHeaderButton = document.createElement('button');
        addHeaderButton.innerHTML = "Add header";
        addHeaderButton.id = "clientaddheaderbutton";
        addHeaderButton.addEventListener('click', function(evt){
            console.log('clicked Add header button');
            var name = evt.target.parentNode.querySelector("#clientresponseheadername").value;
            var value = evt.target.parentNode.querySelector("#clientresponseheadervalue").value;
            if(name === null || name.length == 0)return;
            var header = {};
            header.name = name;
            header.value = value;
            this.clientheaders.push(header);
            document.getElementById('clientheaders').innerHTML += name + ": " + value + "<br>";
        }.bind(this));
        requestDiv.appendChild(addHeaderButton);

        var sendRequestButton = document.createElement('button');
        sendRequestButton.innerHTML = "Send";
        sendRequestButton.id = "clientsendbutton";
        requestDiv.appendChild(sendRequestButton);

        this.rootElement.appendChild(requestDiv);

        var serverSidePerceivedRequestDiv = document.createElement('div');

        var perceivedRequestHeading = document.createElement('h3');
        perceivedRequestHeading.innerHTML = "Server side perceived request (as received)";
        serverSidePerceivedRequestDiv.appendChild(perceivedRequestHeading);

        var perceivedRequestPre = document.createElement('pre');
        perceivedRequestPre.id = "clientperceivedrequest";
        serverSidePerceivedRequestDiv.appendChild(perceivedRequestPre);

        this.rootElement.appendChild(serverSidePerceivedRequestDiv);

        var responseDiv = document.createElement('div');

        var responseHeading = document.createElement('h3');
        responseHeading.innerHTML = "Response";
        responseDiv.appendChild(responseHeading);

        var responseP = document.createElement('p');
        responseP.id = "clientstatuscode";

        var clientstatuscodelabel = document.createElement('span');
        clientstatuscodelabel.innerHTML = "Response status code: ";
        clientstatuscodelabel.id = "clientstatuscodelabel";
        responseP.appendChild(clientstatuscodelabel);

        var clientstatuscodevalue = document.createElement('span');
        clientstatuscodevalue.classList.add('responsevalue');
        clientstatuscodevalue.id = "clientstatuscodevalue";
        responseP.appendChild(clientstatuscodevalue);

        responseP.appendChild(document.createElement('br'));

        var responsetimelabel = document.createElement('span');
        responsetimelabel.innerHTML = "Response time: ";
        responseP.appendChild(responsetimelabel);

        var responseTimeSpan = document.createElement('span');
        responseTimeSpan.id = "responsetime";
        responseP.appendChild(responseTimeSpan);

        responseDiv.appendChild(responseP);

        var responseHeaders = document.createElement('p');
        var responseHeadersLabel = document.createElement('span');
        responseHeadersLabel.innerHTML = "Headers:";
        responseHeaders.appendChild(responseHeadersLabel);
        var responseHeadersField = document.createElement('span');
        responseHeadersField.id = "clientresponseheaders";
        responseHeaders.appendChild(responseHeadersField);
        responseDiv.appendChild(responseHeaders);

        var clientresponsebodyP = document.createElement('p');
        clientresponsebodyP.id = "clientresponsebody";
        
        var clientresponsebodyLabel = document.createElement('span');
        clientresponsebodyLabel.innerHTML = "Body:<br>";
        clientresponsebodyP.appendChild(clientresponsebodyLabel);

        var clientresponsebodyPre = document.createElement('pre');
        clientresponsebodyPre.id = "clientresponsebodyvalue";
        clientresponsebodyPre.classList.add("responsevalue");
        clientresponsebodyP.appendChild(clientresponsebodyPre);

        responseDiv.appendChild(clientresponsebodyP);

        var clearButton = document.createElement('button');
        clearButton.innerHTML = "Clear";
        clearButton.addEventListener('click', function(evt){
            evt.target.parentNode.parentNode.querySelector('#clientresponseheaders').innerHTML = '';
            evt.target.parentNode.parentNode.querySelector('#clientperceivedrequest').innerHTML = '';
            evt.target.parentNode.parentNode.querySelector('#clientstatuscodevalue').innerHTML = '';
            evt.target.parentNode.parentNode.querySelector('#clientresponsebodyvalue').innerHTML = '';
            evt.target.parentNode.parentNode.querySelector('#responsetime').innerHTML = '';
    
        });
        responseDiv.appendChild(clearButton);

        this.rootElement.appendChild(responseDiv);

        var inputs = this.rootElement.querySelectorAll('input');
        for(var i = 0; i < inputs.length; i++){
            inputs[0].addEventListener('change', function(evt){
                console.log('changed value: "' + evt.target.value + '"');
            });
        }

        this.rootElement.querySelector('#clientsendbutton').addEventListener('click', function(evt) {
            console.log('clicked Send button');
            var method = evt.target.parentNode.querySelector("#clientmethod").value;
            var body = evt.target.parentNode.querySelector("#clientbody").value;
            var body = evt.target.parentNode.querySelector("#clientbody").value;
            var endpoint = evt.target.parentNode.querySelector("#clientendpoint").value;
            if(method === null) {
                evt.target.parentNode.querySelector("#clientmethod").style.backgroundColor = 'red';
                setTimeout(function(){
                    evt.target.parentNode.querySelector("#clientmethod").style.backgroundColor = 'white';
                }, 200);
                return;
            }
            var xhttp = new XMLHttpRequest();
            xhttp.onreadystatechange = function () {
                if (xhttp.readyState == 4) {
                    var divContainerElement = this.target.parentNode.parentNode;
                    divContainerElement.querySelector("#clientstatuscodevalue").innerHTML = xhttp.status + " " + xhttp.statusText;
                    divContainerElement.querySelector("#clientresponsebodyvalue").innerHTML = textToHtml(xhttp.responseText);
                    var headers = xhttp.getAllResponseHeaders();
                    if(headers !== null) headers = headers.replace('\r\n', '<br>');
                    divContainerElement.querySelector("#clientresponseheaders").innerHTML = headers;
                }
            }.bind(evt);
            for(var i = 0; i < clientheaders.length; i++){
                xhttp.setRequestHeader(clientheaders[i].name, clientheaders[i].value);
            }
            xhttp.open(method, endpoint, false);
            var startTime = new Date().getTime();
            xhttp.send(body);
            evt.target.parentNode.parentNode.querySelector("#responsetime").innerHTML = (+(new Date().getTime()) - +startTime) + " ms";
            var xhttp2 = new XMLHttpRequest();
            xhttp2.onreadystatechange = function () {
                if (xhttp2.readyState == 4) {
                    this.target.parentNode.parentNode.querySelector("#clientperceivedrequest").innerHTML = textToHtml(xhttp2.responseText);
                }
            }.bind(evt);
            xhttp2.open("GET", "/kgshfdlkgd", false);
            xhttp2.setRequestHeader('LastRequestProbe', 'Yes');
            xhttp2.send();
        });

    }


}
