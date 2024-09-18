document.addEventListener("DOMContentLoaded", function() {
  var form = document.getElementById("wipe-form");
  
  form.addEventListener("submit", function(event) {
      event.preventDefault(); 
      
      var account = document.getElementById("user-id").value;
      var code = document.getElementById("reset-code").value;
      var after = document.getElementById("reset-time").value;
      
      var requestData = {
          userId: account,
          reset: code,
          after: after
      };
      
      fetch("/api/reset", {
          method: "POST",
          headers: {
              "Content-Type": "application/json"
          },
          body: JSON.stringify(requestData)
      })
      .then(function(response) {
        if(response.ok){
          Swal.fire(
            'Success',
            'Account successfully wiped',
            'success'
          );
            account.value='';
            code.value='';
        }else{
          response.json().then(data => {
            Swal.fire(
              'Failed',
              data.message,
              'error'
            );
          });
        }
      }).catch(function(error) {
        console.log(error);
        Swal.fire(
          'Failed',
          'Unable to exucute wipe with provided input',
          'error'
        );
      });
  });
});