import React from "react";
import { useAuth } from "../context/AuthContext.jsx";

export default function ExcursionCard({ excursion, onBook }) {
    const { user } = useAuth();
    const isLoggedIn = user && user.username !== null;

    return (
        <div className="bg-white border border-gray-300 shadow-lg rounded-md p-6 font-mono h-full">
            <div className="flex flex-col justify-between h-full">
                <div>
                    <div className="text-center mb-4">
                        <h2 className="text-2xl font-bold">Zoo Excursion</h2>
                        <hr className="my-3 border-gray-400"/>
                        <p className="text-sm font-semibold">Excursion #{excursion.id}</p>
                        <p className="text-lg font-bold mt-1">Topic: {excursion.topic}</p>
                        <p className="text-md mt-1">Guide: {excursion.guide?.name}</p>
                    </div>

                    <div className="text-sm space-y-2">
                        <p>Date: {excursion.date}</p>
                        <p>Start Time: {excursion.startTime}</p>
                        <p>Duration: {excursion.durationMinutes} minutes</p>
                        <p>Max Participants: {excursion.maxParticipants}</p>
                        <p>Booked: {excursion.bookedCount}</p>
                    </div>

                    <hr className="my-4 border-gray-400"/>
                    <p className="text-xs text-gray-700 mb-4">{excursion.description}</p>
                </div>

                <div>
                    <div className="border-t border-gray-400 pt-3 text-right text-lg font-bold">
                        Available:{" "}
                        {excursion.maxParticipants - excursion.bookedCount}
                    </div>

                    {isLoggedIn ? (
                        <div className="mt-4 flex justify-center">
                            <button
                                onClick={() => onBook(excursion)}
                                className="bg-green-600 text-white px-4 py-2 rounded-full hover:bg-green-700 transition"
                            >
                                Book Now
                            </button>
                        </div>
                    ) :
                    <p className="pt-2 text-center text-red-600 text-sm space-y-2 italic">Log in to book this excursion</p>}
                </div>
            </div>
        </div>
    );
}
