class PreparedHttpResponse{
    constructor(){
        this.id = uuidv4();
        this.delay = 0;
        this.httpResponse = {};
        this.httpResponse.headers = [];
        this.filters = [];
    }
}

