import { Component } from '@angular/core';
import { PaymentsService } from '../payments.service';

declare let Stripe : any;

@Component({
  selector: 'app-payments',
  templateUrl: './payments.component.html',
  styleUrls: ['./payments.component.css']
})

export class PaymentsComponent {
  amount : number = 10;
  transactionId? : string
  stripe = Stripe("pk_test_51OMogvF1fgtfUhRcaoBSEBydP5c1O1M3V7aVjaznscIR54sbUl9gj2R7GsedypiwDbUOk6CUuN2VDUNrhdlx9KR9005NuU6DBb")


  constructor(private paymentService: PaymentsService) {

  }

  requestPrepayment() {
    this.paymentService.prepay(this.amount).subscribe({
      next : (response : any) => {
        this.transactionId = response.client_secret
        this.showForm()
      },
      error : (response : any) => {
        console.log(response)
      }
    })
    
  }

  showForm() {
    let elements = this.stripe.elements()
    let style = {
      base: {
        color: "#32325d", fontFamily: 'Arial, sans-serif',
        fontSmoothing: "antialiased", fontSize: "16px",
        "::placeholder": {
        color: "#32325d"
        }
      },

      invalid: {
        fontFamily: 'Arial, sans-serif', color: "#fa755a",
        iconColor: "#fa755a"
        }
    }

    let card = elements.create("card", { style : style })
    card.mount("#card-element")
    card.on("change", function(event : any) {
      document.querySelector("button")!.disabled = event.empty;
      document.querySelector("#card-error")!.textContent = event.error ? event.error.message : "";
    });

    let self = this
    let form = document.getElementById("payment-form");
    form!.addEventListener("submit", function(event) {
      event.preventDefault();
      self.payWithCard(card);
    });
    
    form!.style.display = "block"
  }

  payWithCard(card : any) {
    let self = this
    this.stripe.confirmCardPayment(this.transactionId, {
      payment_method: {
        card: card
      }
    }).then(function(response : any) {
      if (response.error) {
        alert(response.error.message);
      } else {
        if (response.paymentIntent.status === 'succeeded') {
          console.log("El pago se ha realizado con éxito");
          self.paymentService.confirm().subscribe({
            next : (response : any) => {
              console.log(response)
            },
            error : (response : any) => {
              console.log(response)
            }
          })
        }
      }
    });
  }
   
     
   
}
