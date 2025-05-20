import React from "react";
import { Link } from "react-router-dom";
import { ArrowUpRight } from "lucide-react";

const EventCard = ({ event }) => {

    return (
        <div className="bg-white border rounded-lg shadow-md hover:shadow-lg transition-all overflow-hidden flex flex-col">
            <img
                src={`/${event.image}`}
                alt={event.title}
                className="w-full h-56 object-cover hover:scale-105 transition-transform duration-300"
            />
            <div className="p-5 flex flex-col flex-grow">
                <Link
                    to={`/view/events/${event.id}`}
                    onClick={() => window.scrollTo({ top: 0, behavior: "smooth" })}
                    className="mt-2 text-3xl font-extrabold text-green-800 flex items-center justify-between hover:text-green-600"
                >
                    {event.title}
                    <ArrowUpRight className="w-5 h-5" />
                </Link>

                <div className="mt-4">
                    <span className="text-gray-500 text-s">{event.date}</span>
                </div>
            </div>
        </div>
    );
};

export default EventCard;
