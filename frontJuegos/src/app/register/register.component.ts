import { Component } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  registerForm = new FormGroup(
    {
      Nombre : new FormControl(''),
      Email : new FormControl(''),
      Pwd1 : new FormControl(''),
      Pdw2 : new FormControl('')
    }
  );

  onSubmit(){
    console.log(this.registerForm.value)
  }
}
