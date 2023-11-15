import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { user } from '../user/user';
import { UserSService } from '../user-s.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  loginForm: FormGroup;

  usuario:user;
  ws!: WebSocket;
  
  constructor(private formBuilder:FormBuilder,private userService : UserSService){
    this.loginForm = this.formBuilder.group({
      Email : ['',[Validators.required, Validators.email]],
      Pwd : ['',[Validators.required, Validators.minLength(6)]]
    },);

    this.usuario = new user;
  };

  onSubmit(){
    console.log(this.loginForm.value);
    this.usuario.datosLogin(this.loginForm.controls['Email'].value,this.loginForm.controls['Pwd'].value);
    this.userService.logearUsuario(this.usuario).subscribe(
      result =>{
        this.ws = new WebSocket("ws://localhost:8080/wsGames?httpId="+ result); ///mirar result.httpId
        console.log(JSON.stringify(result));
      },
      error => {
        alert(error)
      });
      

    /*
    this.userService.comprobarSession().subscribe((data)=>
    {
      console.log(JSON.stringify(data));
    });
    */
  }
}
