import { useEffect, useState } from "react";
import { getTopPrices } from "../services/api";

const PriceList = () => {
    const [prices, setPrices] = useState<Record<string, number>>({});

    useEffect(() => {
        const fetchPrices = async () => {
            const res = await getTopPrices();
            setPrices(res.data);
        };

        fetchPrices();
        const interval = setInterval(fetchPrices, 5000);
        return () => clearInterval(interval);
    }, []);

    return (
        <ul>
            { Object.entries(prices).map(([symbol, price]) => (
                <li key={symbol}>
                    {symbol}: ${price}
                </li>
            )) }
        </ul>
    );
};

export default PriceList;