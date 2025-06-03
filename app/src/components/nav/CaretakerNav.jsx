import {Link} from "react-router-dom";
import {Utensils} from "lucide-react";

export default function CaretakerNav() {
    return (
        <>
            <Link to="/my/feedings" className="flex items-center gap-1 hover:text-green-200 transition">
                <Utensils className="w-5 h-5"/>
                My feedings
            </Link>
        </>
    );
}
