import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RayaComponent } from './raya/raya.component';
import { RegisterComponent } from './register/register.component';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { LoginComponent } from './login/login.component';
import { ElegirJuegoComponent } from './elegir-juego/elegir-juego.component';
import { SalaDeEsperaComponent } from './sala-de-espera/sala-de-espera.component';


@NgModule({
  declarations: [
    AppComponent,
    RayaComponent,
    RegisterComponent,
    LoginComponent,
    ElegirJuegoComponent,
    SalaDeEsperaComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
