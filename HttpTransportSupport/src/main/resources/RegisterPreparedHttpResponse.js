class RegisterPreparedHttpResponse {

    constructor(preparedStatement){
        if(preparedStatement === null) preparedStatement = new PreparedHttpResponse();
        this.preparedStatement = preparedStatement;

        this.clientheaders = [];
        this.rootElement = document.createElement('div');
        this.rootElement.id = "new";
        
        this.rootElement.classList.add('additiongui');
        var marginElement = document.createElement('div');
        marginElement.style.margin = "20px";

        var heading = document.createElement('h2');
        heading.innerHTML = "Prepared HTTP response";
        marginElement.appendChild(heading);
        
        var div1 = document.createElement('div');
        
        var responseHeading = document.createElement('h3');
        responseHeading.innerHTML = "Response to send";
        div1.appendChild(responseHeading);

        var responsePanel = document.createElement('div');
        responsePanel.classList.add('panel');

        responsePanel.appendChild(this.responseBodyPanel());

        var advancedHeading = document.createElement('h4');
        advancedHeading.style.cursor = "pointer";
        advancedHeading.id="advancedHeading"
        advancedHeading.style.marginTop = "25px";
        advancedHeading.innerHTML = "Advanced &#709;";
        advancedHeading.addEventListener('click', function(){
            if(document.getElementById('advancedHttp').style.display == 'block'){
                document.getElementById('advancedHttp').style.display = 'none';
                document.getElementById('advancedHeading').innerHTML = 'Advanced &#709;';
            }else{
                document.getElementById('advancedHttp').style.display = 'block';
                document.getElementById('advancedHeading').innerHTML = 'Advanced &#708;';
            }
        });
        responsePanel.appendChild(advancedHeading);

        responsePanel.appendChild(this.advancedPanel());


        var filterPanel = document.createElement('div');
        filterPanel.classList.add('panel');
        filterPanel.id = "responsefilters";
        filterPanel.innerHTML = "<h3 class='tooltip'>Triggers for when to send this response<span class='tooltiptext'>All registered triggers for a prepared response need to be fullfilled for the registered response to be sent. If no trigger filter is applied the response will always be sent (if no other response are sent first).</span></h3><div id='registeredtriggers'>";
        var innertext = '';
        for(var i = 0; i < this.preparedStatement.filters.length; i++){
            innertext += this.preparedStatement.filters[i].type;
            if(this.preparedStatement.filters[i].type != 'next')
                innertext += ": ";

            if(this.preparedStatement.filters[i].field1)
                innertext += "'" + this.preparedStatement.filters[i].field1 + "'";

            if(this.preparedStatement.filters[i].field2)
                innertext += ", '" + this.preparedStatement.filters[i].field2 + "'";

            innertext += "<br>";
        }
        filterPanel.innerHTML += innertext;

        filterPanel.innerHTML += "</div>";

        var triggerTable = document.createElement('table');
        triggerTable.id = "triggertable";
        var triggerTypeRow = document.createElement('tr');
        triggerTypeRow.classList.add('noborder');
        var triggerTypeCell = document.createElement('td');
        triggerTypeCell.classList.add('noborder');
        var triggerTypeSelect = document.createElement("select");
        triggerTypeSelect.id = 'triggertype';
        triggerTypeCell.innerHTML = "<select id='triggertype' >";
        triggerTypeSelect.innerHTML += "<option value='NextResponse'>Next response</option>";
        triggerTypeSelect.innerHTML += "<option value='OriginUrlFilter'>Origin URL</option>";
        triggerTypeSelect.innerHTML += "<option value='HttpMethodVerbFilter'>HTTP method verb</option>";
        triggerTypeSelect.innerHTML += "<option value='EndpointUrlFilter'>End-point path</option>";
        triggerTypeSelect.innerHTML += "<option value='HeaderExistsFilter'>Header exist</option>";
        triggerTypeSelect.innerHTML += "<option value='HeaderValueFilter'>Header value</option>";
        triggerTypeSelect.innerHTML += "<option value='TimePeriodFilter'>Time period</option>";
        triggerTypeSelect.innerHTML += "<option value='BodyRegexMatchFilter'>Body regular expression match</option>";
        triggerTypeSelect.innerHTML += "<option value='BodyContainsMatchFilter'>Body contains</option>";
        triggerTypeRow.appendChild(triggerTypeSelect);
        triggerTable.appendChild(triggerTypeRow);

        var triggerFieldsRow = document.createElement('tr');
        var triggerFieldsTable = document.createElement('table');
        var field1Row = document.createElement('tr');
        var field1Cell = document.createElement('td');
        field1Cell.classList.add('noborder');
        field1Cell.innerHTML = "<label id='field1label' for='field1'></label><input id='field1' type='text'>";
        field1Row.appendChild(field1Cell);

        var field1Cell2 = document.createElement('td');
        var infoIcon = document.createElement('img');
        infoIcon.classList.add('infoIcon');
        infoIcon.src = "data:image/png;base64,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";
        infoIcon.alt = "info";

        field1Cell2.appendChild(infoIcon);

        var field1Cell2div = document.createElement('div');
        field1Cell2div.innerHTML = "<span id='triggertypedescription'></span><br />";
        //field1Cell2div.innerHTML += "<button onclick=\"document.getElementById('triggertypedescription').style.display = 'none';this.style.display='none';\">Close</button>";
        field1Cell2.appendChild(field1Cell2div);

        field1Row.appendChild(field1Cell2);

        triggerFieldsTable.appendChild(field1Row);

        var field2row = document.createElement('tr');

        field2row.innerHTML = "<td class='noborder'><label id='field2label' for='field2'></label><input id='field2' type='text'></td>";

        triggerFieldsTable.appendChild(field2row);

        triggerFieldsRow.appendChild(triggerFieldsTable);

        triggerTable.appendChild(triggerFieldsRow);

        var submitButtonRow = document.createElement('tr');
        submitButtonRow.innerHTML = "<td class='noborder'><button id='addRequestFilter' onclick='addRequestFilter()'>Add</button></td>";
        triggerTable.appendChild(submitButtonRow);

        filterPanel.appendChild(triggerTable);
        filterPanel.appendChild(document.createElement('br'));

        responsePanel.appendChild(filterPanel);
        div1.appendChild(responsePanel);
        marginElement.appendChild(div1);
        
        var submitBtn = document.createElement('button');
        submitBtn.id = "addfilter";
        submitBtn.onclick = "submit()";
        submitBtn.innerHTML = "Submit";
        marginElement.appendChild(submitBtn);

        this.rootElement.appendChild(marginElement);

        this.updateFilterList();

        triggerTypeSelect.addEventListener('change', function(){
            var triggertype = triggerTypeSelect.value;
            switch(triggertype) {
                case 'NextResponse':
                    document.getElementById('field1').value = "";
                    document.getElementById('field2').value = "";
                    document.getElementById('field1label').innerHTML = "";
                    document.getElementById('field1label').style.display = 'none';
                    document.getElementById('field1').style.display = 'none';
                    document.getElementById('field2label').innerHTML = "";
                    document.getElementById('field2label').style.display = 'none';
                    document.getElementById('field2').style.display = 'none';
                    document.getElementById('triggertypedescription').innerHTML = 'Sends the request above once only, as the next server response.';
                    break;
                case 'OriginUrlFilter':
                    document.getElementById('field1').value = "";
                    document.getElementById('field2').value = "";
                    var xhttp = new XMLHttpRequest();
                    xhttp.onreadystatechange = function () {
                        if (this.readyState == 4 && this.status == 200) {
                            var ipaddress = "e.g. 'http://127.0.0.1:8080'";
                            console.log("Last response:\n\rResponse code: " + this.status + "\n\rResponse body content:\n\r" + this.responseText + "\n\r");
                            if(this.responseText !== null){
                                ipaddress = "your address is '" + this.responseText + "'";
                            }
                            document.getElementById('field1label').innerHTML = "Origing URL (" + ipaddress + "):";
                        }
                    };
                    xhttp.open("GET", "/api/ip", true);
                    xhttp.send();
                    document.getElementById('field1label').style.display = 'block';
                    document.getElementById('field1').style.display = 'block';
                    document.getElementById('field2label').innerHTML = "";
                    document.getElementById('field2label').style.display = 'none';
                    document.getElementById('field2').style.display = 'none';
                    document.getElementById('triggertypedescription').innerHTML = 'Checks the Origin header for the incomming request and if it matches the registered response is sent.';
                    break;
                case 'EndpointUrlFilter':
                    document.getElementById('field1').value = "";
                    document.getElementById('field2').value = "";
                    document.getElementById('field1label').innerHTML = "End-point path (e.g. '/api/v1/resourses/persons'):";
                    document.getElementById('field1label').style.display = 'block';
                    document.getElementById('field1').style.display = 'block';
                    document.getElementById('field2label').innerHTML = "";
                    document.getElementById('field2label').style.display = 'none';
                    document.getElementById('field2').style.display = 'none';
                    document.getElementById('triggertypedescription').innerHTML = 'Parses the end-point path of the incoming request. If it matches the registered response is sent.';
                    break;
                case 'HeaderExistFilter':
                    document.getElementById('field1').value = "";
                    document.getElementById('field2').value = "";
                    document.getElementById('field1label').innerHTML = "Header name to exist:";
                    document.getElementById('field1label').style.display = 'block';
                    document.getElementById('field1').style.display = 'block';
                    document.getElementById('field2label').innerHTML = "";
                    document.getElementById('field2label').style.display = 'none';
                    document.getElementById('field2').style.display = 'none';
                    document.getElementById('triggertypedescription').innerHTML = 'Checks if the header with the given name is found on the incoming request. If it does the registered response is sent.';
                    break;
                case 'TimePeriodFilter':
                    document.getElementById('field1').value = "";
                    document.getElementById('field2').value = "";
                    document.getElementById('field1label').innerHTML = "Start time (HH:mm:SS):";
                    document.getElementById('field1label').style.display = 'block';
                    document.getElementById('field1').style.display = 'block';
                    document.getElementById('field2label').innerHTML = "Stop time (HH:mm:SS):";
                    document.getElementById('field2label').style.display = 'block';
                    document.getElementById('field2').style.display = 'block';
                    document.getElementById('triggertypedescription').innerHTML = 'Checks time (server side) for the incoming request. If current time is between given start time and stop time the registered response is sent.';
                    break;
                case 'HeaderValueFilter':
                    document.getElementById('field1').value = "";
                    document.getElementById('field2').value = "";
                    document.getElementById('field1label').innerHTML = "Header name:";
                    document.getElementById('field1label').style.display = 'block';
                    document.getElementById('field1').style.display = 'block';
                    document.getElementById('field2label').innerHTML = "Header value:";
                    document.getElementById('field2label').style.display = 'block';
                    document.getElementById('field2').style.display = 'block';
                    document.getElementById('triggertypedescription').innerHTML = 'Checks the incoming request for a header matching the given parameters. If it does the registered response is sent.';
                    break;
                case 'BodyRegexMatchFilter':
                    document.getElementById('field1').value = "";
                    document.getElementById('field2').value = "";
                    document.getElementById('field1label').innerHTML = "Regular expression pattern for match:";
                    document.getElementById('field1label').style.display = 'block';
                    document.getElementById('field1').style.display = 'block';
                    document.getElementById('field2label').innerHTML = "";
                    document.getElementById('field2label').style.display = 'none';
                    document.getElementById('field2').style.display = 'none';
                    document.getElementById('triggertypedescription').innerHTML = 'Matches the incoming request body with the given pattern. If a match is found the registered response is sent.';
                    break;
                case 'HttpMethodVerbFilter':
                    document.getElementById('field1').value = "";
                    document.getElementById('field2').value = "";
                    document.getElementById('field1label').innerHTML = "HTTP request verb to match:";
                    document.getElementById('field1label').style.display = 'block';
                    document.getElementById('field1').style.display = 'block';
                    document.getElementById('field2label').innerHTML = "";
                    document.getElementById('field2label').style.display = 'none';
                    document.getElementById('field2').style.display = 'none';
                    document.getElementById('triggertypedescription').innerHTML = 'Checks the incoming request body if the given verb (GET, POST, DELETE, PUT, etc.) matches this field value. If a match is found the registered response is sent.';
                    break;
                case 'BodyContainsMatchFilter':
                    document.getElementById('field1').value = "";
                    document.getElementById('field2').value = "";
                    document.getElementById('field1label').innerHTML = "String to find:";
                    document.getElementById('field1label').style.display = 'block';
                    document.getElementById('field1').style.display = 'block';
                    document.getElementById('field2label').innerHTML = "";
                    document.getElementById('field2label').style.display = 'none';
                    document.getElementById('field2').style.display = 'none';
                    document.getElementById('triggertypedescription').innerHTML = 'Checks the incoming request body if it contains the given string. If a match is found the registered response is sent.';
                    break;
                default:
                    document.getElementById('field1').value = "";
                    document.getElementById('field2').value = "";
                    document.getElementById('field1label').innerHTML = "Oups. This should not happen...!";
                    document.getElementById('field1label').style.display = 'block';
                    document.getElementById('field1').style.display = 'block';
                    document.getElementById('field1').value = 'OUPS!';
                    document.getElementById('field2label').innerHTML = "";
                    document.getElementById('field2label').style.display = 'none';
                    document.getElementById('field2').style.display = 'none';
                    document.getElementById('triggertypedescription').innerHTML = 'Nooooo... Needs updating.';
                    break;
            }
        });
    }
    
    updateHeadersList(){
        var div = document.getElementById('headervalues');
        div.innerHTML = '';
        if(this.preparedStatement.httpResponse.headers.length === 0){
            div.style.display = 'none';
        } else {
            div.style.display = 'block';
        }
        for(var i = 0; i < this.preparedStatement.httpResponse.headers.length; i++){
            div.innerHTML += this.preparedStatement.httpResponse.headers[i].name + ": " + this.preparedStatement.httpResponse.headers[i].value + "<br>";
        }
    }

    submit() {
        if(!this.preparedStatement.httpResponse) this.preparedStatement.httpResponse = {};
        if(document.getElementById('bodycontenttypedropdown').value == 'direct'){
            this.preparedStatement.httpResponse.body = htmlToText(document.getElementById('responsebodycontent').value);
            this.preparedStatement.httpResponse.bodyType = 'direct';
        } else {
            this.preparedStatement.httpResponse.bodyFilePath = htmlToText(document.getElementById('bodycontentfilepath').value);
            this.preparedStatement.httpResponse.bodyType = 'file';
        }
        this.preparedStatement.httpResponse.responseCode = document.getElementById('responsecode').value;
        this.preparedStatement.delay = document.getElementById('delay').value;
        postToApi();
        document.getElementById('new').style.display = 'none';
        document.getElementById('addPreparedResponse').style.display = 'block';
    }

    responseBodyPanel(){
        var responseBodyPanel = document.createElement('div');
        responseBodyPanel.id = "responsebody";
        responseBodyPanel.innerHTML = '<h4>Response</h4><h5>Body content:</h5>';
        var bodyTypeSelect = document.createElement('select');
        bodyTypeSelect.id = 'bodycontenttypedropdown';
        bodyTypeSelect.innerHTML = '<option value="direct" selected>Direct input</option><option value="file">File read by server at runtime</option></select>';
        bodyTypeSelect.addEventListener('change', function(){
            if(this.value == 'direct'){
                document.getElementById('bodycontentfilepathlabel').style.display = 'none';
                document.getElementById('bodycontentfilepath').style.display = 'none';
                document.getElementById('responsebodycontent').style.display = 'block';
            } else {
                document.getElementById('bodycontentfilepathlabel').style.display = 'block';
                document.getElementById('bodycontentfilepath').style.display = 'block';
                document.getElementById('responsebodycontent').style.display = 'none';
            }
        });
        var options = bodyTypeSelect.querySelector('option');
        for(var i = 0; i < options.length; i++){
            if(options[i].getAttribute('value') == this.preparedStatement.httpResponse.bodyType){
                options[i].selected;
            }
        }
        responseBodyPanel.appendChild(bodyTypeSelect);
        var recordButton = document.createElement('button');
        recordButton.id = "recordButton";
        recordButton.innerHTML = "Record...";
        recordButton.onclick = "new Overlay('Record response', new RecordClient().rootElement, this;);";
        responseBodyPanel.appendChild(recordButton);
        var responsebodycontent = document.createElement('textarea');
        responsebodycontent.id = "responsebodycontent";
        responsebodycontent.innerHTML = this.preparedStatement.httpResponse.body;
        responsebodycontent.rows = "5";
        responseBodyPanel.appendChild(responsebodycontent);

        var bodycontentfilepathlabel = document.createElement('label');
        bodycontentfilepathlabel.id = "bodycontentfilepathlabel";
        bodycontentfilepathlabel.for = "bodycontentfilepath";
        bodycontentfilepathlabel.innerHTML = "<br>Path to response content file (read at runtime by the service virtualization server):";
        responseBodyPanel.appendChild(bodycontentfilepathlabel);

        var bodycontentfilepath = document.createElement('input');
        bodycontentfilepath.id = "bodycontentfilepath";
        bodycontentfilepath.value = this.preparedStatement.httpResponse.bodyFilePath;
        bodycontentfilepath.type = "text";
        responseBodyPanel.appendChild(bodycontentfilepath);

        return responseBodyPanel;
    }


    advancedPanel(){
        var delay = 0;
        if(this.preparedStatement.delay) delay = this.preparedStatement.delay;
        var responseCode = 200;
        if(this.preparedStatement.httpResponse.responseCode) responseCode = this.preparedStatement.httpResponse.responseCode;
        var advancedPanel = document.createElement('div');
        advancedPanel.id = "advancedHttp";
        advancedPanel.innerHTML = '<table id="responsecodesection">';
        advancedPanel.innerHTML += '<tr><td>Response code for registered response:</td><td><input id="responsecode" type="number" value=' + responseCode + '></td></tr>';
        advancedPanel.innerHTML += '<tr><td>Server side response delay (ms):</td><td><input id="delay" type="number" value=' + delay + '></td></tr></table>';
        advancedPanel.innerHTML += '<hr /><div id="responseheaders"><h4>Register response headers</h4><div id="headervalues">';
        for(var i = 0; i < this.preparedStatement.httpResponse.headers.length; i++){
            advancedPanel.innerHTML += this.preparedStatement.httpResponse.headers[i].name + ": " + this.preparedStatement.httpResponse.headers.value + "<br>";
        }
        advancedPanel.innerHTML += '</div>';
        advancedPanel.innerHTML += '<table><tr><td><label for="responseheadername">Header name</label><input type="text" id="responseheadername"><br><label for="responseheadervalue">Header value</label><input type="text" id="responseheadervalue"><br></td><td><button id="addheaderbutton" onclick="addheader()">Add</button></td></tr></table>';
        advancedPanel.innerHTML += '</div>';
        return advancedPanel;
    }

    addRequestFilter(){
        var triggertype = document.getElementById('triggertype').value;
        if(document.getElementById('field1').style.display == 'block' && document.getElementById('field1').value.length === 0) {
            document.getElementById('field1').style.backgroundColor = 'red';
            setTimeout(function(){
                document.getElementById('field1').style.backgroundColor = 'white';
            }, 200);
            return;
        }
        if(document.getElementById('field2').style.display == 'block' && document.getElementById('field2').value.length === 0) {
            document.getElementById('field2').style.backgroundColor = 'red';
            setTimeout(function(){
                document.getElementById('field2').style.backgroundColor = 'white';
            }, 200);
            return;
        }
        var filter = {};
        filter.type = triggertype;
        filter.field1 = document.getElementById('field1').value;
        filter.field2 = document.getElementById('field2').value;
        this.preparedStatement.filters.push(filter);
        this.updateFilterList();
    }

    
    updateFilterList(){
        var div = document.getElementById('registeredtriggers');
        if(div === null)return;
        if(this.preparedStatement.filters.length === 0){
            div.style.display = 'none';
        } else {
            div.style.display = 'block';
        }
        div.innerHTML = '';
        var innertext = '';
        for(var i = 0; i < this.preparedStatement.filters.length; i++){
            innertext += this.preparedStatement.filters[i].type;
            if(this.preparedStatement.filters[i].type != 'next')
                innertext += ": ";

            if(this.preparedStatement.filters[i].field1)
                innertext += "'" + this.preparedStatement.filters[i].field1 + "'";

            if(this.preparedStatement.filters[i].field2)
                innertext += ", '" + this.preparedStatement.filters[i].field2 + "'";

            innertext += "<br>";
        }
        div.innerHTML = innertext;
    }


    clientaddheader(){
        var name = document.getElementById('clientresponseheadername').value;
        var value = document.getElementById('clientresponseheadervalue').value;
        if(name === null || name.length > 0)return;
        var header = {};
        header.name = name;
        header.value = value;
        this.clientheaders.push(header);
        document.getElementById('clientheaders').innerHTML += name + ": " + value + "<br>";
    }

}

class RecordClient{
    constructor(){
        this.rootElement = document.createElement('div');
        this.rootElement.id = "recordClient";
        var recordClientHeading = document.createElement('h2');
        recordClientHeading.innerHTML = "HTTP record client";
        recorderDiv.appendChild(recordClientHeading);



        var div1_1 = document.createElement('div');
        div1_1.innerHTML = '<h3>Request</h3><label for="recordendpoint">URL: </label><input id="recordendpoint" type="text" value="http://"><label for="recordmethod">Method:</label><input id="recordmethod" type="text" value="GET"><label for="recordbody">Body</label><textarea id="recordbody"></textarea>Headers:<br><span id="recordheaders"></span><label for="recordresponseheadername">Header name</label><input type="text" id="recordresponseheadername"><br><label for="recordresponseheadervalue">Header value</label><input type="text" id="recordresponseheadervalue"><br>';

        
        var addHeaderButton = document.createElement('button');
        addHeaderButton.id = "recordaddheaderbutton";
        addHeaderButton.innerHTML = "Add header";
        addHeaderButton.addEventListener('click', function(){
            alert('strange');
        });
        div1_1.appendChild(addHeaderButton);

        var sendButton = document.createElement('button');
        sendButton.id = "recordsendbutton";
        sendButton.innerHTML = "Send";
        sendButton.addEventListener('click', function(evt) {
            var xhttp = new XMLHttpRequest();
            xhttp.onreadystatechange = function () {
                if (xhttp.readyState == 4) {
                    this.target.parentNode.parentNode.parentNode.querySelector('#saverecordedbutton').disabled = false;
                    this.target.parentNode.parentNode.parentNode.querySelector('#clearrecordedresponsebutton').disabled = false;
                    this.target.parentNode.parentNode.parentNode.querySelector('#recordresponsebodyvalue').innerHTML = textToHtml(this.responseText);
                    this.target.parentNode.parentNode.parentNode.querySelector('#recordstatuscodevalue').innerHTML = this.status;
                    console.log("Last response:\n\rResponse code: " + this.status + "\n\rResponse body content:\n\r" + this.responseText + "\n\r");
                }
            };
            xhttp.open(this.target.parentNode.parentNode.parentNode.querySelector('#recordmethod').value, "/sgsdfjfdsfgf", false);
            xhttp.setRequestHeader('ServiceVirtualizationForward', document.getElementById('recordendpoint').value);
            xhttp.send(this.target.parentNode.parentNode.parentNode.querySelector('#recordbody').value);
        }.bind(evt));
        div1_1.appendChild(sendButton);

        recorderDiv.appendChild(div1_1);

        var responseDiv = document.createElement('div');
        var responseDivHeading = document.createElement('h3');
        responseDivHeading.innerHTML = "Response";
        responseDiv.appendChild(responseDivHeading);

        var recordstatuscode = document.createElement('p');
        recordstatuscode.id = "recordstatuscode";
        var recordstatuscodelabel = document.createElement('span');
        recordstatuscodelabel.id = "recordstatuscodelabel";
        recordstatuscodelabel.innerHTML = "Response status code: ";
        recordstatuscode.appendChild(recordstatuscodelabel);
        var recordstatuscodevalue = document.createElement('span');
        recordstatuscodevalue.id = "recordstatuscodevalue";
        recordstatuscodevalue.classList.add('responsevalue');
        recordstatuscode.appendChild(recordstatuscodevalue);
        responseDiv.appendChild(recordstatuscode);

        var headersP = document.createElement('p');
        headersP.innerHTML = '<span>Headers:</span><span id="recordresponseheaders"></span>';
        responseDiv.appendChild(headersP);

        var bodyP = document.createElement('p');
        bodyP.id = "recordresponsebody";
        bodyP.innerHTML = '<span>Body:<br></span><pre class="responsevalue" id="recordresponsebodyvalue"></pre>';
        responseDiv.appendChild(bodyP);

        var clearrecordedresponsebutton = document.createElement('button');
        clearrecordedresponsebutton.id = "clearrecordedresponsebutton";
        clearrecordedresponsebutton.disabled = true;
        clearrecordedresponsebutton.innerHTML = "Clear";
        clearrecordedresponsebutton.addEventListener('click', function(){
            document.getElementById('recordresponseheaders').innerHTML = '';
            document.getElementById('recordstatuscodevalue').innerHTML = '';
            document.getElementById('recordresponsebodyvalue').innerHTML = '';
            document.getElementById('saverecordedbutton').disabled = true;
            document.getElementById('clearrecordedresponsebutton').disabled = true;    
        });
        responseDiv.appendChild(clearrecordedresponsebutton);

        var saverecordedbutton = document.createElement('button');
        saverecordedbutton.id = "saverecordedbutton";
        saverecordedbutton.disabled = true;
        saverecordedbutton.innerHTML = "Transfer to add dialogue and close";
        saverecordedbutton.addEventListener('click', function(){
            document.getElementById("responsebodycontent").value = htmlToText(document.getElementById('recordresponsebodyvalue').innerHTML);
            document.getElementById("bodycontenttypedropdown").value = "direct";
            document.getElementById("responsecode").value = document.getElementById('recordstatuscodevalue').innerHTML;
            document.getElementById('recordClient').style.display = 'none';
        });
        responseDiv.appendChild(saverecordedbutton);

        this.rootElement.appendChild(responseDiv);
    }
}