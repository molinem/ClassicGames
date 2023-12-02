import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RayaComponent } from './raya/raya.component';
import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';
import { ElegirJuegoComponent } from './elegir-juego/elegir-juego.component';
import { SalaDeEsperaComponent } from './sala-de-espera/sala-de-espera.component';

const routes: Routes = [
  {path:'4EnRaya',component:RayaComponent},
  {path:'Register',component:RegisterComponent},
  {path:'Login',component:LoginComponent},
  {path:'ElegirJuego',component:ElegirJuegoComponent},
  {path: 'SalaDeEspera', component:SalaDeEsperaComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
