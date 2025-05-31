import { Link } from "react-router-dom";
import { Map, PartyPopper } from "lucide-react";

export default function EventManagerNav() {
    return (
        <>
            <Link to="/excursions" className="flex items-center gap-1 hover:text-green-200 transition">
                <Map className="w-5 h-5" />
                Excursions
            </Link>
            <Link to="/events" className="flex items-center gap-1 hover:text-green-200 transition">
                <PartyPopper className="w-5 h-5" />
                Events
            </Link>
        </>
    );
}
