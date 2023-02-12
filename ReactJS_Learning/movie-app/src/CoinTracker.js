import { useEffect } from "react";
import { useState } from "react";

function CoinTracker() {
  const [loading, setLoading] = useState(true);
  // 기본값이 비어있으면,
  // 아래 coins.length에서 타입을 알 수 없으므로,
  // 오류를 반환한다
  // 즉, 기본값은 미리 지정해주는 것이 안전하다
  const [coins, setCoins] = useState([]);
  useEffect(() => {
    fetch("https://api.coinpaprika.com/v1/tickers")
      .then((response) => response.json())
      .then((json) => {
        setCoins(json);
        setLoading(false);
      });
  }, []);
  return (
    <div>
      <h1>The Coins! {loading ? "" : `(${coins.length})`}</h1>
      {loading ? (
        <strong>Laoding...</strong>
      ) : (
        <select>
          {coins.map((coin) => (
            <option>
              {coin.name} ({coin.symbol}): ${coin.quotes.USD.price} USD
            </option>
          ))}
        </select>
      )}
    </div>
  );
}
