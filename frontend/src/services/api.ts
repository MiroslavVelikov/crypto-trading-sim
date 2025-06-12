import axios from "axios";

const API = axios.create({
    baseURL: "http://localhost:8080/api",
});

export const getTopPrices = () => API.get("/prices");
export const placeOrder = (data: any) => API.post("/orders", data);