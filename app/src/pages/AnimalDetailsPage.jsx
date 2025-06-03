import React, {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {
    ArrowLeft,
    PawPrint,
    Leaf,
    Globe,
    Venus,
    Calendar,
    MapPin,
    Stethoscope,
    HeartPulse,
    UtensilsCrossed} from "lucide-react";
import LoadingPage from "./LoadingPage.jsx";
import {fetchData} from "../utils/fetch.js";
import {useLoading} from "../utils/useLoading.jsx";
import {useAuth} from "../context/AuthContext.jsx";

export default function AnimalDetailsPage() {
    const [animal, setAnimal] = useState([]);
    const navigate = useNavigate();
    const {id} = useParams();
    const {loading, start, stop} = useLoading();
    const { user } = useAuth();
    const isAdmin = user?.roles.includes("ADMIN");

    useEffect(() => {
        if (animal?.name) {
            const targetSection = document.querySelector(".scroll-target");
            if (targetSection) {
                targetSection.scrollIntoView({behavior: "smooth"});
            }
        }
    }, [animal]);

    useEffect(() => {
        const fetchAnimal = async () => {
            await fetchData({
                url: `/api/animals/${id}`,
                onSuccess: (data) => setAnimal(data || []),
                errorMessage: "Failed to load animal data",
                navigate,
                onStart: start,
                onFinally: stop
            });
        };

        fetchAnimal();
    }, [id, navigate]);

    if (loading) {
        return <LoadingPage/>;
    }

    return (
        <div className="min-h-screen bg-gradient-to-b from-green-50 to-green-100 py-10 px-4 scroll-target">
            <div className="max-w-4xl mx-auto bg-white rounded-2xl shadow-lg overflow-hidden border border-green-200">
                <div className="p-4">
                    <button
                        onClick={() => navigate(-1)}
                        className="w-10 h-10 rounded-full bg-green-100 text-green-700 hover:bg-green-200 transition flex items-center justify-center shadow"
                        title="Back"
                    >
                        <ArrowLeft className="w-5 h-5"/>
                    </button>
                </div>

                <div className="px-8 pb-10">
                    <h1 className="text-5xl text-center font-extrabold text-green-900 mb-6 font-serif leading-snug">
                        {animal.name}
                    </h1>

                    {animal.image && (
                        <img
                            src={`/${animal.image}`}
                            alt={animal.name}
                            className="mx-auto max-w-full max-h-80 object-cover rounded-xl mb-8 shadow-md border"
                        />
                    )}

                    <div className="space-y-4 text-green-900 text-lg font-medium">
                        <p className="flex items-center gap-3">
                            <PawPrint className="w-5 h-5 text-green-700"/>
                            <span className="font-extrabold">Species:</span> {animal.species}
                        </p>
                        <p className="flex items-center gap-3">
                            <Leaf className="w-5 h-5 text-green-700"/>
                            <span className="font-extrabold">Animal Group:</span> {animal.animalGroup}
                        </p>
                        <p className="flex items-center gap-3">
                            <Globe className="w-5 h-5 text-green-700"/>
                            <span className="font-extrabold">Habitat Type:</span> {animal.habitatType}
                        </p>
                        <p className="flex items-center gap-3">
                            <Venus className="w-5 h-5 text-green-700"/>
                            <span className="font-extrabold">Gender:</span> {animal.gender}
                        </p>
                        <p className="flex items-center gap-3">
                            <Calendar className="w-5 h-5 text-green-700"/>
                            <span className="font-extrabold">Date of birth:</span> {animal.birthDate}
                        </p>
                        {animal.enclosure && (
                            <p className="flex items-center gap-3">
                                <MapPin className="w-5 h-5 text-green-700"/>
                                <span className="font-extrabold">Enclosure:</span>{' '}
                                {`${animal.enclosure.name} | ${animal.enclosure.location} | ${animal.enclosure.areaM2}m²`}
                            </p>
                        )}

                        {isAdmin && (
                            <>
                                <p className="flex items-center gap-3">
                                    <HeartPulse className="w-5 h-5 text-green-700"/>
                                    <span className="font-extrabold">Health Status:</span> {animal.healthStatus}
                                </p>
                                <p className="flex items-center gap-3">
                                    <Stethoscope className="w-5 h-5 text-green-700"/>
                                    <span className="font-extrabold">Last Checked Up At:</span> {animal.lastCheckedUpAt}
                                </p>
                                <p className="flex items-center gap-3">
                                    <UtensilsCrossed className="w-5 h-5 text-green-700"/>
                                    <span className="font-extrabold">Last Fed Up At:</span> {animal.lastFedUpAt}
                                </p>
                                <div className="flex justify-center gap-3 pt-10">
                                    <button
                                        className="bg-green-600 hover:bg-green-700 text-white px-3 py-1 rounded-md text-lg font-semibold"
                                    >
                                        View Medical Records
                                    </button>
                                    <button
                                        onClick={() => navigate(`/animals/feedings/${e.id}`)}
                                        className="bg-indigo-600 hover:bg-indigo-700 text-white px-3 py-1 rounded-md text-lg font-semibold"
                                    >
                                        View Feeding
                                    </button>
                                </div>
                            </>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}
