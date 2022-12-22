const app = new Vue({
    el: '#app',
    data: {
        id: "",
        firstName: "",
        lastName: "",
        email: "",
        outPut: "",
        clients: [],
        accounts: []
    },
    methods:{
        // load and display JSON sent by server for /clients
        loadData: function() {
            axios.get("/api/clients")
            .then(function (response) {
                // handle success
                app.outPut = response.data;
                app.clients = response.data;
            })
            .catch(function (error) {
                // handle error
                app.outPut = error;
            })
        },
        // load and display JSON sent by server for /clients/{id}
        loadAccountsData: function(id) {
            const params = new URLSearchParams();
            params.append('id', id);
            let url = "/api/clients/" + id;
            //axios.get("/api/clients",params.toString())
            axios.get(url)
            .then(function (response) {
                // handle success
                console.log("/api/clients/" + id);
                console.log("params: " + params);
                app.outPut = response.data;
                app.accounts = response.data.accounts;
            })
            .catch(function (error) {
                // handle error
                app.outPut = error;
            })
        },
         // handler for when user clicks add client
        addClient: function() {
            if (app.email.length > 1 && app.firstName.length > 1 && app.lastName.length > 1) {
                this.postClient(app.email,app.firstName,app.lastName);
            }
        },
        // code to post a new client using AJAX
        // on success, reload and display the updated data from the server
        postClient: function(email, firstName, lastName) {
             axios.post("clients",{ "email":email, "firstName": firstName, "lastName": lastName })
            .then(function (response) {
                // handle success
                showOutput = "Saved -- reloading";
                app.loadData();
                app.clearData();
            })
            .catch(function (error) {
                // handle error
                app.outPut = error;
            })
        },
        clearData: function(){
            app.firstName = "";
            app.lastName = "";
            app.email = "";
        }
    },
    mounted(){
        this.loadData();
    }
});

