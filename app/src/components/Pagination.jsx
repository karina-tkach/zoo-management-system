import React, { useEffect } from "react";

const Pagination = ({
                        currentPage,
                        totalPages,
                        setCurrentPage,
                        shouldScroll,
                        setShouldScroll,
                    }) => {
    useEffect(() => {
        if (shouldScroll) {
            const targetSection = document.querySelector(".scroll-target");
            if (targetSection) {
                targetSection.scrollIntoView({ behavior: "smooth" });
            }
            setShouldScroll(false);
        }
    }, [currentPage, shouldScroll, setShouldScroll]);

    const renderPageNumbers = () => {
        let pages = [];

        if (totalPages <= 6) {
            // show all pages if total pages <= 6
            pages = Array.from({ length: totalPages }, (_, i) => i + 1);
        } else {
            // show first 3, ellipsis, last 3 pages
            pages = [1, 2, 3, "...", totalPages - 2, totalPages - 1, totalPages];
        }

        return pages.map((page, index) => (
            <button
                key={index}
                onClick={() => {
                    if (page !== "...") {
                        setCurrentPage(page);
                        setShouldScroll(true);
                    }
                }}
                className={`px-3 py-1 rounded-lg disabled:opacity-50 ${
                    currentPage === page ? "bg-gray-200 font-bold" : "hover:bg-gray-100"
                }`}
                disabled={page === "..."}
            >
                {page}
            </button>
        ));
    };

    return (
        <div className="mt-8">
            <div className="hidden md:flex justify-center items-center space-x-2">
                <button
                    onClick={() => {
                        setCurrentPage(Math.max(currentPage - 1, 1));
                        setShouldScroll(true);
                    }}
                    disabled={currentPage === 1}
                    className="px-3 py-1 text-black hover:bg-gray-100 rounded-lg flex items-center disabled:opacity-50"
                    aria-label="Previous page"
                >
                    ← Previous
                </button>

                {renderPageNumbers()}

                <button
                    onClick={() => {
                        setCurrentPage(Math.min(currentPage + 1, totalPages));
                        setShouldScroll(true);
                    }}
                    disabled={currentPage === totalPages}
                    className="px-3 py-1 text-black hover:bg-gray-100 rounded-lg flex items-center disabled:opacity-50"
                >
                    Next →
                </button>
            </div>
        </div>
    );
};

export default Pagination;
