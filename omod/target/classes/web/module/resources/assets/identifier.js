/**
 * Created by Muthoni on 29/05/15.
 */

function CheckPatient(val){
    var element=document.getElementById('ID');
    if(val=='ID')
        element.style.display='block';
    else
        element.style.display='none';

    var element=document.getElementById('clinicNo');
    if(val=='clinicNo')
        element.style.display='block';
    else
        element.style.display='none';
}
