import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RayaComponent } from './raya/raya.component';
import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';
import { ElegirJuegoComponent } from './elegir-juego/elegir-juego.component';
import { MesaComponent } from './mesa/mesa.component';
import { HistorialComponent } from './historial/historial.component';

const routes: Routes = [
  {path:'4EnRaya',component:RayaComponent},
  {path:'Register',component:RegisterComponent},
  {path:'Login',component:LoginComponent},
  {path:'ElegirJuego',component:ElegirJuegoComponent},
  {path: 'Escoba', component:MesaComponent},
  {path:'Historial', component:HistorialComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
