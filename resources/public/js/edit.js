

function namechng(){

const namechngbtn = document.getElementById("namesub");
const nametxtbox= document.getElementById("nametxtbox");
nametxtbox.setAttribute("type","text");
namechngbtn.style.display ="inline-block";
}

function namevalidate(event){
    event.preventDefault();
    const nametxtbox= document.getElementById("nametxtbox");
    var valid = nametxtbox.checkValidity();
    if (!valid){
        alert("Name is Invalid!!");
        htmx.trigger('#namesub', 'htmx:abort')
    }

}


function agechng(){

    const agechngbtn = document.getElementById("agesub");
    const agetxtbox= document.getElementById("agetxtbox");
    agetxtbox.setAttribute("type","text");
    agechngbtn.style.display ="inline-block";
}
    


function agevalidate(){
    const agetxtbox= document.getElementById("agetxtbox");
    var valid = agetxtbox.checkValidity();
    if (!valid){
    alert("Age is Invalid!!");
    htmx.trigger('#agesub', 'htmx:abort')}
}


function phonechng(){

    const phonechngbtn = document.getElementById("phonesub");
    const phonetxtbox= document.getElementById("phonetxtbox");
    phonetxtbox.setAttribute("type","text");
    phonechngbtn.style.display ="inline-block";
    }
    

function phonevalidate(){
    const phonetxtbox= document.getElementById("phonetxtbox");
    var valid = phonetxtbox.checkValidity();
    if (!valid){
    alert("Phone is Invalid!!");
    htmx.trigger('#phonesub', 'htmx:abort')}
}


    
function addresschng(){

    const addresschngbtn = document.getElementById("addresssub");
    const addresstxtbox= document.getElementById("addresstxtbox");
    addresstxtbox.setAttribute("type","text");
    addresschngbtn.style.display ="inline-block";
    }
    
function addressvalidate(){
    const addresstxtbox= document.getElementById("addresstxtbox");
    var valid = addresstxtbox.checkValidity();
    if (!valid){
    alert("Address is Invalid!!");
    htmx.trigger('#addresssub', 'htmx:abort')}
}