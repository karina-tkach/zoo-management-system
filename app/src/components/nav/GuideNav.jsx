import { Link } from "react-router-dom";
import { Map, PartyPopper } from "lucide-react";

export default function GuideNav() {
    return (
        <>
            <Link to="/guide/excursions" className="flex items-center gap-1 hover:text-green-200 transition">
                <Map className="w-5 h-5" />
                My Excursions
            </Link>
        </>
    );
}
