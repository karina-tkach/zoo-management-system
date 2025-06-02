import React from "react";
import { Link } from "react-router-dom";
import { PawPrint, MapPin, Info, ArrowUpRight } from "lucide-react";

const AnimalCard = ({ animal }) => {
    return (
        <div className="bg-white rounded-2xl border border-green-100 shadow-md hover:shadow-xl transition-all overflow-hidden flex flex-col">
            {animal.image && (
                <img
                    src={`/${animal.image}`}
                    alt={animal.name}
                    className="w-full h-60 object-cover hover:scale-105 transition-transform duration-300"
                />
            )}

            <div className="p-5 flex flex-col flex-grow">
                <Link
                    to={`/view/animals/${animal.id}`}
                    onClick={() => window.scrollTo({ top: 0, behavior: "smooth" })}
                    className="text-2xl font-bold text-green-800 flex items-center justify-between hover:text-green-600"
                >
                    {animal.name}
                    <ArrowUpRight className="w-5 h-5" />
                </Link>

                <p className="mt-1 text-gray-600 flex items-center gap-2">
                    <Info className="w-4 h-4 text-green-500" />
                    Group: {animal.animalGroup}
                </p>

                <p className="mt-1 text-gray-600 flex items-center gap-2">
                    <MapPin className="w-4 h-4 text-green-500" />
                    Habitat: {animal.habitatType}
                </p>
            </div>
        </div>
    );
};

export default AnimalCard;
