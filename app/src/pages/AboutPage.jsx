import { Leaf, HeartHandshake, Users, BookOpen } from "lucide-react";

export default function AboutPage() {
    return (
        <div className="min-h-screen bg-gradient-to-br from-green-50 to-green-100 px-6 py-12 flex flex-col items-center">
            <div className="max-w-4xl w-full bg-white p-8 rounded-lg shadow-md">
                <h1 className="text-4xl font-bold text-green-800 mb-6 text-center">
                    About Our Zoo
                </h1>
                <p className="text-gray-700 mb-6 text-lg text-center">
                    Our Zoo is more than just a place to see animals—it's a vibrant hub of conservation,
                    education, and community. With a passion for wildlife and the environment,
                    we strive to inspire every visitor to care more deeply about our planet.
                </p>

                <div className="grid grid-cols-1 sm:grid-cols-2 gap-8">
                    <div className="flex items-start gap-4">
                        <Leaf className="w-8 h-8 text-green-600 mt-1" />
                        <div>
                            <h3 className="text-xl font-semibold text-green-700">
                                Conservation First
                            </h3>
                            <p className="text-gray-700">
                                We actively participate in breeding programs and wildlife rescue,
                                helping protect endangered species.
                            </p>
                        </div>
                    </div>

                    <div className="flex items-start gap-4">
                        <HeartHandshake className="w-8 h-8 text-green-600 mt-1" />
                        <div>
                            <h3 className="text-xl font-semibold text-green-700">
                                Ethical Care
                            </h3>
                            <p className="text-gray-700">
                                All our animals are cared for with the highest welfare standards by
                                passionate zookeepers and veterinarians.
                            </p>
                        </div>
                    </div>

                    <div className="flex items-start gap-4">
                        <Users className="w-8 h-8 text-green-600 mt-1" />
                        <div>
                            <h3 className="text-xl font-semibold text-green-700">
                                Community & Events
                            </h3>
                            <p className="text-gray-700">
                                We host regular school trips, workshops, and events to connect
                                people with wildlife in meaningful ways.
                            </p>
                        </div>
                    </div>

                    <div className="flex items-start gap-4">
                        <BookOpen className="w-8 h-8 text-green-600 mt-1" />
                        <div>
                            <h3 className="text-xl font-semibold text-green-700">
                                Education & Outreach
                            </h3>
                            <p className="text-gray-700">
                                Learn about biodiversity, habitats, and conservation through
                                our interactive exhibits and educational programs.
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
