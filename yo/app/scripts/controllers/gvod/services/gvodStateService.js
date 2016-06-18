angular.module('partialsApplication').factory('gvodStateService',[function(){
        
        var state = {

            identifier : "",
            filename : "",
            url :"http://localhost",
            port : "8080",

            setIdentifier : function (id) {
                this.identifier = id;
            },

            setFilename : function (name) {
                this.filename = name;
            },

            getIdentifier : function () {
                return this.identifier;
            },

            getFilename : function () {
                return this.filename;
            },

            setPort : function (port) {
                this.port = port;
            },

            setURL : function (url) {
                this.url = url;
            },

            getURL : function(){
                return this.url;
            },

            getPort : function () {
                return this.port;
            }

        };


        return state;
        
    }]);
