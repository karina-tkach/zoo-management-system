import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { ArrowLeft } from "lucide-react";
import {fetchData} from "../utils/fetch.js";
import {useLoading} from "../utils/useLoading.jsx";
import LoadingPage from "./LoadingPage.jsx";

export default function Ticket() {
    const [ticket, setTicket] = useState([]);
    const { id } = useParams();
    const navigate = useNavigate();
    const { loading, start, stop } = useLoading();


    useEffect(() => {
        const fetchTicket = async () => {
            await fetchData({
                url: `/api/tickets/${id}`,
                onSuccess: (ticket) => setTicket(ticket || []),
                errorMessage: "Failed to load ticket data",
                navigate,
                onStart: start,
                onFinally: stop,
            });
        };

        fetchTicket();
    }, [id, navigate]);

    if (loading) {
        return <LoadingPage/>;
    }

    return (
        <div className="min-h-screen bg-gray-100 py-5 px-4 flex justify-center">
            <div className="bg-white text-black w-[360px] p-6 border border-gray-300 shadow-lg font-mono">
                <div className="text-center">
                    <h1 className="text-xl font-bold tracking-wide">Zoo Park</h1>
                </div>

                <hr className="my-4"/>

                <p className="text-center text-sm text-gray-700">Ticket #{ticket.id}</p>

                <hr className="mb-4"/>

                <div className="flex justify-between text-sm font-bold mb-2">
                    <span>Item</span>
                    <span>Price</span>
                </div>
                <div className="flex justify-between text-sm mb-4">
                    <span>{ticket.ticketType} Ticket</span>
                    <span>{(ticket.price)}</span>
                </div>
                <hr className="my-4"/>

                <div className="text-sm mb-2">
                    <p className="font-bold">UUID: {ticket.uuid}</p>
                    <p>Full Name: {ticket.fullName}</p>
                    <p>Visit Type: {ticket.visitType}</p>
                    {ticket.excursionName && <p>Excursion topic: {ticket.excursionName}</p>}
                    {ticket.excursionId && <p>Excursion id: {ticket.excursionId}</p>}
                    <p>Visit Date: {ticket.visitDate}</p>
                </div>

                <hr className="my-4"/>

                <div className="text-sm">
                    <p>Payment: {ticket.purchaseMethod}</p>
                </div>

                <div className="my-6 text-center">
                    <div className="flex justify-center gap-[2px] my-6">
                        {Array.from({length: 40}).map((_, i) => (
                            <div
                                key={i}
                                className={`h-16 ${
                                    i % 7 === 0
                                        ? "w-[2px] bg-black"
                                        : i % 5 === 0
                                            ? "w-[1px] bg-gray-800"
                                            : "w-[1px] bg-black"
                                }`}
                            />
                        ))}
                    </div>
                    {/* Barcode placeholder */}
                </div>

                <p className="text-xs text-center mt-4 text-gray-500">
                    Purchased on {new Date(ticket.purchaseTime).toLocaleString()}
                </p>

                <button
                    onClick={() => navigate("/tickets")}
                    className="mt-6 w-full bg-gray-200 hover:bg-gray-300 text-gray-700 text-sm py-2 rounded"
                >
                    <ArrowLeft className="inline w-4 h-4 mr-1"/> Back to Tickets
                </button>
            </div>
        </div>
    );
}
