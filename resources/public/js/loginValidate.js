
const loginButton = document.getElementById("loginbtn");
loginButton.addEventListener("click", function(e) {
    const uname= document.getElementById("uname").value;  
    const password= document.getElementById("password").value;  



    if (uname==null || uname==""){  
        alert("Name can't be blank"); 
        e.preventDefault(); 
        return false;  
      }
      
      
      else if(password.length==0 || password==null){  
        alert("Password cant be empty"); 
        e.preventDefault(); 
        return false;  
        }  
      }




);