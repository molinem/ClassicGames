import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AgGridModule } from 'ag-grid-angular';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RayaComponent } from './raya/raya.component';
import { RegisterComponent } from './register/register.component';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { LoginComponent } from './login/login.component';
import { ElegirJuegoComponent } from './elegir-juego/elegir-juego.component';
import { SalaDeEsperaComponent } from './sala-de-espera/sala-de-espera.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { FormsModule } from '@angular/forms';
import { ChatComponent } from './chat/chat.component';
import { MesaComponent } from './mesa/mesa.component';
import { ManoComponent } from './mano/mano.component';
import { HistorialComponent } from './historial/historial.component';
import { CurrencyPipe, UpperCasePipe } from '@angular/common';

@NgModule({
  declarations: [
    AppComponent,
    RayaComponent,
    RegisterComponent,
    LoginComponent,
    ElegirJuegoComponent,
    SalaDeEsperaComponent,
    ChatComponent,
    MesaComponent,
    ManoComponent,
    HistorialComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatSnackBarModule,
    AgGridModule
  ],
  providers: [
    UpperCasePipe,
  CurrencyPipe   
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
