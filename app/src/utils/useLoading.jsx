import { useState } from "react";

export const useLoading = () => {
    const [loading, setLoading] = useState(false);
    const start = () => setLoading(true);
    const stop = () => setLoading(false);
    return { loading, start, stop };
};
