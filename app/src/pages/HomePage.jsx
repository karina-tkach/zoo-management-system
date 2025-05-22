import React, { useRef, useCallback } from "react";
import { Swiper, SwiperSlide } from "swiper/react";
import { Navigation, Autoplay } from "swiper/modules";
import {
    ArrowLeft,
    ArrowRight,
    PawPrint,
    Leaf,
    Globe,
    Users,
    Smile,
} from "lucide-react";
import "swiper/css";
import "swiper/css/navigation";

import zoo1 from "../assets/zoo1.jpg";
import zoo2 from "../assets/zoo2.jpg";
import zoo3 from "../assets/zoo3.jpg";
import zoo4 from "../assets/zoo4.jpg";
import zoo5 from "../assets/zoo5.jpg";
import {useAuth} from "../context/AuthContext.jsx";

const images = [
    { id: 1, src: zoo1, alt: "Lion resting" },
    { id: 2, src: zoo2, alt: "Elephants walking" },
    { id: 3, src: zoo3, alt: "Colorful parrots" },
    { id: 4, src: zoo4, alt: "Giraffes eating leaves" },
    { id: 5, src: zoo5, alt: "Pandas playing" },
];

export default function HomePage() {
    const sliderRef = useRef(null);
    const {loading} = useAuth();

    const handlePrev = useCallback(() => {
        if (sliderRef.current) {
            sliderRef.current.swiper.slidePrev();
        }
    }, []);

    const handleNext = useCallback(() => {
        if (sliderRef.current) {
            sliderRef.current.swiper.slideNext();
        }
    }, []);

    if (loading)
        return (
            <div className="relative p-6 min-h-screen bg-gray-200">

                <div className="absolute inset-0 bg-white/80 backdrop-blur-md flex items-center justify-center z-50">
                    <div className="text-center">
                        <div className="animate-spin rounded-full h-16 w-16 border-t-4 border-blue-600 border-solid mx-auto mb-4" />
                        <p className="text-xl font-semibold text-gray-700">Loading...</p>
                    </div>
                </div>
            </div>
        );

    return (
        <div
            className="min-h-screen bg-gradient-to-br from-green-50 to-green-100 px-6 py-12 flex flex-col items-center">
            <header className="mb-10 max-w-3xl text-center">
                <h1 className="text-5xl font-extrabold text-green-800 mb-4">
                    Welcome to Our Zoo
                </h1>
                <p className="text-green-700 text-lg max-w-xl mx-auto">
                    Discover the wonders of wildlife up close. Explore, learn, and enjoy
                    unforgettable experiences with animals from around the world.
                </p>
            </header>

            <div className="w-full max-w-5xl mb-10">
                <Swiper
                    ref={sliderRef}
                    modules={[Navigation, Autoplay]}
                    spaceBetween={20}
                    slidesPerView={1}
                    breakpoints={{
                        640: {slidesPerView: 1},
                        768: {slidesPerView: 2},
                        1024: {slidesPerView: 3},
                    }}
                    loop={true}
                    autoplay={{delay: 4000, disableOnInteraction: false}}
                >
                    {images.map(({id, src, alt}) => (
                        <SwiperSlide key={id} className="overflow-hidden rounded-lg">
                            <img
                                src={src}
                                alt={alt}
                                className="w-full h-64 object-cover transition-transform duration-500 hover:scale-105"
                                loading="lazy"
                            />
                        </SwiperSlide>
                    ))}
                </Swiper>
                <div className="flex justify-center items-center gap-4 mt-6">
                    <button
                        className="border border-gray-200 w-[50px] h-[50px] p-3 rounded-full shadow hover:bg-gray-300 transition-all"
                        onClick={handlePrev}
                        aria-label="Previous Slide"
                    >
                        <ArrowLeft className="w-6 h-6 text-gray-500"/>
                    </button>
                    <button
                        className="border border-gray-200 w-[50px] h-[50px] p-3 rounded-full shadow hover:bg-gray-300 transition-all"
                        onClick={handleNext}
                        aria-label="Next Slide"
                    >
                        <ArrowRight className="w-6 h-6 text-gray-500"/>
                    </button>
                </div>
            </div>

            {/* Article section */}
            <section className="max-w-4xl bg-white p-8 rounded-lg shadow-md">
                <h2 className="text-3xl font-bold text-green-800 mb-6 text-center">
                    Why Visit Our Zoo?
                </h2>
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-6">
                    <div className="flex items-start gap-4">
                        <PawPrint className="w-8 h-8 text-green-600 mt-1"/>
                        <div>
                            <h3 className="text-xl font-semibold text-green-700">
                                Meet Amazing Animals
                            </h3>
                            <p className="text-gray-700">
                                From majestic lions to playful pandas, witness exotic wildlife
                                in thoughtfully designed habitats.
                            </p>
                        </div>
                    </div>
                    <div className="flex items-start gap-4">
                        <Leaf className="w-8 h-8 text-green-600 mt-1"/>
                        <div>
                            <h3 className="text-xl font-semibold text-green-700">
                                Support Conservation
                            </h3>
                            <p className="text-gray-700">
                                Your visit helps fund vital conservation efforts to protect
                                endangered species.
                            </p>
                        </div>
                    </div>
                    <div className="flex items-start gap-4">
                        <Globe className="w-8 h-8 text-green-600 mt-1"/>
                        <div>
                            <h3 className="text-xl font-semibold text-green-700">
                                Learn About the Planet
                            </h3>
                            <p className="text-gray-700">
                                Educational programs, excursions, and interactive zones for all
                                ages to learn about biodiversity.
                            </p>
                        </div>
                    </div>
                    <div className="flex items-start gap-4">
                        <Users className="w-8 h-8 text-green-600 mt-1"/>
                        <div>
                            <h3 className="text-xl font-semibold text-green-700">
                                Perfect for Families
                            </h3>
                            <p className="text-gray-700">
                                A fun, safe, and engaging day out for kids and adults alike.
                                Create lasting memories together.
                            </p>
                        </div>
                    </div>
                    <div className="flex items-start gap-4 sm:col-span-2">
                        <Smile className="w-8 h-8 text-green-600 mt-1"/>
                        <div>
                            <h3 className="text-xl font-semibold text-green-700">
                                Enjoy Nature and Relax
                            </h3>
                            <p className="text-gray-700">
                                Stroll through lush gardens, picnic in green spaces, and
                                reconnect with nature in a calm and beautiful environment.
                            </p>
                        </div>
                    </div>
                </div>
            </section>
            <section className="max-w-6xl mt-20 px-4 sm:px-6 lg:px-8 w-full">
                <div className="bg-white p-10 rounded-2xl shadow-lg">
                    <h2 className="text-4xl font-bold text-green-800 mb-10 text-center">
                        What Makes Us Special
                    </h2>
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                        <div className="flex items-start gap-5 bg-green-50 p-5 rounded-xl hover:shadow-md transition">
                            <Globe className="w-8 h-8 text-green-700 mt-1 shrink-0"/>
                            <div>
                                <h3 className="text-xl font-semibold text-green-800 mb-1">
                                    Eco-Friendly Zoo
                                </h3>
                                <p className="text-gray-700">
                                    We use solar energy, biodegradable materials, and prioritize sustainability in
                                    everything we do.
                                </p>
                            </div>
                        </div>
                        <div className="flex items-start gap-5 bg-green-50 p-5 rounded-xl hover:shadow-md transition">
                            <PawPrint className="w-8 h-8 text-green-700 mt-1 shrink-0"/>
                            <div>
                                <h3 className="text-xl font-semibold text-green-800 mb-1">
                                    Rare Species
                                </h3>
                                <p className="text-gray-700">
                                    Home to some of the world’s rarest animals you won’t easily see anywhere else.
                                </p>
                            </div>
                        </div>
                        <div className="flex items-start gap-5 bg-green-50 p-5 rounded-xl hover:shadow-md transition md:col-span-2">
                            <Users className="w-8 h-8 text-green-700 mt-1 shrink-0"/>
                            <div>
                                <h3 className="text-xl font-semibold text-green-800 mb-1">
                                    Interactive Experiences
                                </h3>
                                <p className="text-gray-700">
                                    Petting zones, feeding sessions, and animal talks for a hands-on adventure.
                                </p>
                            </div>
                        </div>
                        <div
                            className="flex items-start gap-5 bg-green-50 p-5 rounded-xl hover:shadow-md transition md:col-span-2">
                            <Smile className="w-8 h-8 text-green-700 mt-1 shrink-0"/>
                            <div>
                                <h3 className="text-xl font-semibold text-green-800 mb-1">
                                    Community Engagement
                                </h3>
                                <p className="text-gray-700">
                                    We organize school trips, volunteer programs, and conservation events to involve
                                    everyone.
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

        </div>
    );
}
