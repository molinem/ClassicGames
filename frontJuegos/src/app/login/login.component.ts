import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { user } from '../user/user';
import { UserSService } from '../user-s.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  loginForm: FormGroup;

  usuario:user;
  ws!: WebSocket;
  
  constructor(private formBuilder:FormBuilder,private userService : UserSService, private router: Router ){
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
        const js = JSON.stringify(result)
        const jsonObj = JSON.parse(js);

        this.usuario.nombre = jsonObj.nick;
        localStorage.setItem('nick', jsonObj.nick);
        this.userService.changeNick(this.usuario.nombre);
        this.router.navigate(['/ElegirJuego']);
      },
      error => {
        console.log("[Login] -> Se ha producido un error al iniciar sesión: "+error);
      });
  }
}
