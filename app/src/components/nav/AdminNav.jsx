import {Link} from "react-router-dom";
import {Users, Tent, PawPrint} from "lucide-react";

export default function AdminNav() {
    return (
        <>
            <Link to="/staff" className="flex items-center gap-1 hover:text-green-200 transition">
                <Users className="w-5 h-5"/>
                Staff
            </Link>
            <Link to="/enclosures" className="flex items-center gap-1 hover:text-green-200 transition">
                <Tent className="w-5 h-5"/>
                Enclosures
            </Link>
            <Link to="/animals" className="flex items-center gap-1 hover:text-green-200 transition">
                <PawPrint className="w-5 h-5"/>
                Animals
            </Link>
        </>
    );
}
