
const change = document.getElementById("changebtn");

change.addEventListener("click", function(e){

    const name= document.getElementById("name").value;  
    const age= document.getElementById("age").value; 
    const phone= document.getElementById("phone").value; 
    const address = document.getElementById("address").value;
    if (name==null || name==""  ){  
        alert("Name can't be blank"); 
        event.preventDefault(); 
        return false;  
      }
      
      
      else if (age==null || age==""){  
        alert("Age can't be blank"); 
        event.preventDefault(); 
        return false;  
      }


      else if (phone==null || phone==""){  
        alert("Age can't be blank"); 
        event.preventDefault(); 
        return false;  
      }

      else if (name.length>40){  
        alert("Max character limit reached for Name."); 
        event.preventDefault(); 
        return false;  
      }
      else if (age.length>2){  
        alert("Max character limit reached for Age."); 
        event.preventDefault(); 
        return false;  
      }
      else if (phone.length>10  || phone.length<10){  
        alert("Enter a valid 10 digit phone number. "); 
        event.preventDefault(); 
        return false;  
      }
      else if (address.length>70){  
        alert("Max character limit reached for Address."); 
        event.preventDefault(); 
        return false;  
      }
}

);