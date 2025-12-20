import {setGlobalOptions} from "firebase-functions";
import {onRequest} from "firebase-functions/https";
import * as logger from "firebase-functions/logger";
import express, {Request, Response} from "express";
import Stripe from "stripe";
import {defineSecret} from "firebase-functions/params";

const stripeSecretKey = defineSecret("STRIPE_SECRET_KEY");

setGlobalOptions({maxInstances: 10});

const app = express();
app.use(express.json());

app.post("/create-payment-intent", async (req: Request, res: Response) => {
  try {
    const {amount} = req.body;

    if (!amount || typeof amount !== "number") {
      return res.status(400).json({error: "Invalid or missing amount"});
    }

    const stripe = new Stripe(stripeSecretKey.value(), {});

    const paymentIntent = await stripe.paymentIntents.create({
      amount,
      currency: "usd",
      automatic_payment_methods: {enabled: true},
    });

    return res.json({clientSecret: paymentIntent.client_secret});
  } catch (error: unknown) {
    const message = error instanceof Error ? error.message : String(error);
    logger.error("Error creating payment intent:", error);
    res.status(400).json({error: message});
    return;
  }
});

export const api = onRequest({secrets: [stripeSecretKey]}, app);
