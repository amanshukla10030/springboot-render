package com.happyghumakkads.config;

import com.happyghumakkads.entity.Package;
import com.happyghumakkads.entity.User;
import com.happyghumakkads.service.PackageService;
import com.happyghumakkads.service.UserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataLoader implements ApplicationRunner {

    private final PackageService packageService;
    private final UserService userService;

    public DataLoader(PackageService packageService, UserService userService) {
        this.packageService = packageService;
        this.userService = userService;
    }

    @Override
    public void run(ApplicationArguments args) {
        preventAllDuplicates();
        seedAllPackages();

        // Seed a demo user for quick testing of the booking form
        if (userService.findByEmail("demo@happyghumakkads.com").isEmpty()) {
            User u = new User("Demo", "User", "demo@happyghumakkads.com",
                    "+91-8447133338", "password");
            userService.saveUser(u);
        }
    }

    private void preventAllDuplicates() {
        clearPackageIfExists("Pachmarhi Tour Package (02 Nights 03 Days)");
        clearPackageIfExists("Pachmarhi Tour Package (03 Nights 04 Days)");
        clearPackageIfExists("Khajuraho with Panna (02 Night 03 Days)");
        clearPackageIfExists("Khajuraho – Orchha – Gwalior Tour");
        clearPackageIfExists("Ujjain – Indore Tour Package");
        clearPackageIfExists("Wild Escape to Kanha (2 Nights / 3 Days)");
        clearPackageIfExists("Kanha (02 Nights), Jabalpur (01 Night)");
        clearPackageIfExists("Bandhavgarh Tour (02 Nights / 03 Days)");
        clearPackageIfExists("Golden Triangle (5 Night/6 Days)");
        clearPackageIfExists("Majestic Gangtok Darjeeling (04 Days)");
        clearPackageIfExists("Manali –Shimla Tour (5 Night / 6 Days)");
        clearPackageIfExists("Srinagar –Leh Tour Package (9 Night / 10 Days)");
        clearPackageIfExists("Andaman Package (5 Nights/6 Days)");
        clearPackageIfExists("Kerala 5N/6D Highlights");
        clearPackageIfExists("Luxury Holiday in Goa 2N/3D");
    }

    private void clearPackageIfExists(String packageName) {
        List<Package> existingPackages = packageService.searchPackages(packageName);
        existingPackages.forEach(pkg -> packageService.deletePackage(pkg.getId()));
    }

    private void seedAllPackages() {
        // Madhya Pradesh Packages
        seedDetailedPachmarhiPackages();
        seedDetailedKhajurahoPackage();
        seedDetailedKhajurahoOrchhaGwaliorTour();
        seedDetailedUjjainIndoreTour();
        seedDetailedKanhaPackages();
        seedDetailedBandhavgarhPackage();

        // Popular Destinations
        seedDetailedGoldenTriangle();
        seedDetailedGangtokDarjeeling();
        seedDetailedManaliShimla();
        seedDetailedSrinagarLeh();
        seedDetailedAndaman();
        seedDetailedKerala();
        seedDetailedGoa();
    }

    private void seedDetailedPachmarhiPackages() {
        // Pachmarhi 2N/3D Package
        seedDetailed("Pachmarhi Tour Package (02 Nights 03 Days)",
                "Madhya Pradesh",
                new BigDecimal("6999"),
                2,
                "/images/pachmarhi.jpg",
                Package.PackageType.DOMESTIC,
                "Experience the serene hill station of Pachmarhi with our comprehensive 2 nights 3 days package. Known as the 'Queen of Satpura', Pachmarhi offers a perfect blend of natural beauty, spiritual significance, and colonial charm.",
                "DAY 1: Arrival at Pipariya – Transfer to Pachmarhi – Local Sightseeing\n" +
                "Upon arrival at Pipariya Railway Station, you will be warmly welcomed and driven to the beautiful hill station of Pachmarhi, nestled in the Satpura range (approx. 1.5-hour drive). After check-in at your hotel and a brief rest, embark on a half-day local sightseeing tour. Visit the iconic Pandav Caves, believed to be the ancient dwellings of the Pandavas during their exile, and then proceed to Bee Fall, a picturesque waterfall ideal for nature photography and relaxation. Later, enjoy a serene walk in Pachmarhi Lake area before returning to the hotel. Dinner & Overnight Stay at Hotel.\n\n" +
                "DAY 2: Full-Day Sightseeing in Pachmarhi\n" +
                "After a hearty breakfast, set out for a full-day exploration of Pachmarhi's scenic and spiritual sites. Start with Jata Shankar Cave, a natural cave dedicated to Lord Shiva, followed by a visit to Gupt Mahadev and Bada Mahadev Caves. Continue to the Reechgarh Caves, a natural rock shelter, and then enjoy the panoramic views from Dhoopgarh, the highest point in Madhya Pradesh, ideal for witnessing a stunning sunset. Wrap up your day with a visit to the sacred Handi Khoh, a deep ravine surrounded by dense forest. Return to your hotel. Dinner & Overnight Stay at Hotel.\n\n" +
                "DAY 3: Pachmarhi – Departure from Pipariya Railway Station\n" +
                "After breakfast, check out from the hotel and spend some leisurely time shopping for local handicrafts or strolling through Pachmarhi's charming bazaars. If time permits, you may also visit the Christ Church, a beautiful colonial-era church known for its architecture and tranquil atmosphere. Later, you will be driven back to Pipariya Railway Station for your onward journey, taking back with you the serene memories and refreshing experiences of Pachmarhi.",
                "• 02 Night Hotel Accommodation on double sharing basis\n" +
                "• Pickup & drop from Railway station\n" +
                "• Morning tea\n" +
                "• Buffet Breakfast, Buffet Dinner\n" +
                "• One Sightseeing of Panchmarhi Non-Forest area By Pvt. Car\n" +
                "• One Sightseeing tour of Panchmarhi Forest Area By Gypsy\n" +
                "• Our assistance at the time of arrival & departure\n" +
                "• Inclusive all taxes\n" +
                "• No any Hidden cost",
                "• Personal expenses including but not limited to laundry, porterage, telephone calls, tips, gratuities, bottled water, and beverages (both alcoholic and non-alcoholic).\n" +
                "• Charges for optional or adventure activities such as rafting, rock climbing, paragliding, and joy rides (e.g., toy train).\n" +
                "• Travel insurance, entrance fees at monuments or sightseeing spots, professional guide services, and snow vehicle charges (if applicable due to weather conditions).\n" +
                "• Additional expenses arising due to unforeseen events such as natural calamities (landslides, road blockages), political unrest, strikes, or other force majeure situations—such costs must be borne directly by the traveler at the time.\n" +
                "• Room heater charges at hotels or resorts.\n" +
                "• Any items or services not explicitly mentioned under the 'Inclusions' section."
        );

        // Pachmarhi 3N/4D Package
        seedDetailed("Pachmarhi Tour Package (03 Nights 04 Days)",
                "Madhya Pradesh",
                new BigDecimal("11999"),
                3,
                "/images/pachmarhi-extended.jpg",
                Package.PackageType.DOMESTIC,
                "Experience the extended charm of Pachmarhi with our comprehensive 3 nights 4 days package. This package allows you to explore more of the hill station's natural wonders, waterfalls, and spiritual sites at a relaxed pace.",
                "DAY 1: Arrival at Pipariya – Transfer to Pachmarhi – Local Sightseeing\n" +
                "Upon arrival at Pipariya Railway Station, you will be greeted by our representative and transferred to Pachmarhi, a picturesque hill station nestled in the Satpura range (approx. 55 km / 1.5 hours). After checking into your hotel and some rest, begin your local sightseeing with a visit to the Pandava Caves, believed to have been shelter for the Pandavas during their exile. Later, explore the Jata Shankar Cave Temple, a sacred natural shrine dedicated to Lord Shiva, and the Handi Khoh, a deep ravine with a mythological backstory. End your day with a serene walk through the local market before returning to the hotel. Dinner & Overnight Stay at Hotel.\n\n" +
                "DAY 2: Pachmarhi Sightseeing (Natural Wonders & Waterfalls)\n" +
                "After breakfast, proceed for a full-day excursion of Pachmarhi's famous natural attractions. Visit the Bee Falls, a refreshing waterfall ideal for a dip and a popular picnic spot. Next, explore Rajat Prapat (Silver Fall) and Apsara Vihar, a gentle waterfall with a shallow pool that was once a favorite of British ladies. Later, head to Dhoopgarh, the highest point in the Satpura range, to witness an unforgettable sunset view. Enjoy your evening at leisure or explore the nearby cafés before returning to your hotel. Dinner & Overnight Stay at Hotel.\n\n" +
                "DAY 3: Spiritual and Cultural Tour of Pachmarhi\n" +
                "Today, after breakfast, begin your day with a visit to the ancient Mahadeo Cave Temple, located amidst dense forests and offering a divine and meditative ambiance. Proceed to Gupt Mahadev and Bada Mahadev, two revered cave temples dedicated to Lord Shiva, known for their mythological significance and tranquil surroundings. In the afternoon, visit the Reechgarh, a natural cave-like formation surrounded by forest, once believed to be a bear's dwelling. End the day with a visit to the Christ Church, a beautiful colonial-era structure showcasing British-era architecture. Return to your hotel. Dinner & Overnight Stay at Hotel.\n\n" +
                "DAY 4: Departure from Pachmarhi to Pipariya\n" +
                "After an early breakfast and hotel check-out, you will be driven back to Pipariya Railway Station for your onward journey. Depart with cherished memories of the serene landscapes, spiritual sites, and colonial charm of Pachmarhi.",
                "• 03 Night Hotel Accommodation on double sharing basis\n" +
                "• Pickup & drop from Railway station\n" +
                "• Morning tea\n" +
                "• Buffet Breakfast, Buffet Dinner\n" +
                "• Two Sightseeing tours of Pachmarhi By Pvt. Car\n" +
                "• One Sightseeing tour of Pachmarhi Forest Area By Gypsy\n" +
                "• Our assistance at the time of arrival & departure\n" +
                "• Inclusive all taxes\n" +
                "• No any Hidden cost",
                "• Personal expenses including but not limited to laundry, porterage, telephone calls, tips, gratuities, bottled water, and beverages (both alcoholic and non-alcoholic).\n" +
                "• Charges for optional or adventure activities such as rafting, rock climbing, paragliding, and joy rides (e.g., toy train).\n" +
                "• Travel insurance, entrance fees at monuments or sightseeing spots, professional guide services, and snow vehicle charges (if applicable due to weather conditions).\n" +
                "• Additional expenses arising due to unforeseen events such as natural calamities (landslides, road blockages), political unrest, strikes, or other force majeure situations.\n" +
                "• Room heater charges at hotels or resorts.\n" +
                "• Any items or services not explicitly mentioned under the 'Inclusions' section."
        );
    }

    private void seedDetailedKhajurahoPackage() {
        // Khajuraho with Panna (02 Night 03 Days)
        seedDetailed("Khajuraho with Panna (02 Night 03 Days)",
                "Madhya Pradesh",
                new BigDecimal("8999"),
                2,
                "/images/khajuraho.jpg",
                Package.PackageType.DOMESTIC,
                "Explore the UNESCO World Heritage Site of Khajuraho and the wildlife sanctuary of Panna National Park with our comprehensive 2 nights 3 days package. Discover ancient temples and spot exotic wildlife in this perfect blend of culture and nature.",
                "DAY 1: Arrival at Khajuraho – Temple Visit & Resort Check-in\n" +
                "Upon arrival at Khajuraho Airport or Railway Station, you will be received by our representative and transferred to your resort. After check-in and some relaxation, begin your exploration of the magnificent Khajuraho temples, a UNESCO World Heritage Site. Visit the Western Group of temples, including the iconic Kandariya Mahadev Temple, Lakshmana Temple, and Vishwanatha Temple. In the evening, attend the Sound and Light Show that narrates the history of Khajuraho. Dinner & Overnight Stay at Resort.\n\n" +
                "DAY 2: Full-Day Exploration of Khajuraho Temples\n" +
                "After an early breakfast, return to Khajuraho for a full day of sightseeing. Visit the UNESCO World Heritage-listed Khajuraho Group of Monuments, known for their exquisite temples adorned with intricate carvings depicting various aspects of life, culture, and spirituality. Explore the Western Group of Temples, home to the iconic Kandariya Mahadev Temple and the Lakshmana Temple. After lunch, continue to the Eastern Group of temples, including the Javari Temple and Vishwanatha Temple. In the evening, enjoy the Sound and Light Show at the Khajuraho temples, which narrates the history of the region. Dinner and overnight stay at the resort.\n\n" +
                "DAY 3: Visit to Raneh Falls & Departure\n" +
                "After breakfast, check out from your hotel and visit Raneh Falls, located just 20 km from Khajuraho, known for its beautiful canyon made of red sandstone and stunning waterfalls. Spend some time enjoying the natural beauty and the tranquil environment. Afterward, you will be transferred to Khajuraho Railway Station or Airport for your onward journey, concluding your 3-day tour with wonderful memories.",
                "• 02 Night Hotel Accommodation on double sharing basis\n" +
                "• Pickup & drop from Railway station\n" +
                "• Morning tea\n" +
                "• Buffet Breakfast, Buffet Dinner\n" +
                "• One Sightseeing of Panna National Park Forest area By Gypsy\n" +
                "• One Sightseeing tour of Khajuraho By Pvt. Car\n" +
                "• Our assistance at the time of arrival & departure\n" +
                "• Inclusive all taxes\n" +
                "• No any Hidden cost",
                "• Personal expenses including but not limited to laundry, porterage, telephone calls, tips, gratuities, bottled water, and beverages (both alcoholic and non-alcoholic).\n" +
                "• Charges for optional or adventure activities such as rafting, rock climbing, paragliding, and joy rides (e.g., toy train).\n" +
                "• Travel insurance, entrance fees at monuments or sightseeing spots, professional guide services, and snow vehicle charges (if applicable due to weather conditions).\n" +
                "• Additional expenses arising due to unforeseen events such as natural calamities (landslides, road blockages), political unrest, strikes, or other force majeure situations—such costs must be borne directly by the traveler at the time.\n" +
                "• Room heater charges at hotels or resorts.\n" +
                "• Any items or services not explicitly mentioned under the 'Inclusions' section."
        );
    }

    private void seedDetailedKhajurahoOrchhaGwaliorTour() {
        // Khajuraho – Orchha – Gwalior Tour Package (5N/6D)
        seedDetailed("Khajuraho – Orchha – Gwalior Tour",
                "Madhya Pradesh",
                new BigDecimal("12999"),
                5,
                "/images/khajuraho-orchha-gwalior.jpg",
                Package.PackageType.DOMESTIC,
                "Discover the architectural marvels and royal heritage of Madhya Pradesh with our comprehensive 5 nights 6 days Khajuraho-Orchha-Gwalior tour. This journey takes you through UNESCO World Heritage Sites, magnificent forts, and ancient temples showcasing India's glorious past.",
                "DAY 1: Arrival in Khajuraho – Local Sightseeing\n" +
                "On arrival at Khajuraho, you will be received and transferred to your hotel. After check-in and freshening up, explore the Western Group of Temples, a UNESCO World Heritage Site, renowned for their intricate sculptures and architectural brilliance, including the Kandariya Mahadev Temple, Lakshmana Temple, and Vishwanatha Temple. In the evening, attend the Light and Sound Show, which narrates the glorious history of Khajuraho. Dinner & Overnight Stay at Hotel.\n\n" +
                "DAY 2: Khajuraho – Eastern & Southern Group of Temples – Optional Panna Visit\n" +
                "After breakfast, visit the Eastern Group of Temples (including Jain temples like Parsvanatha and Adinath) and the Southern Group of Temples. Later, you may opt for a half-day visit to Panna National Park for a wildlife safari or explore Raneh Falls, known for its dramatic red-rock canyon. Return to Khajuraho. Dinner & Overnight Stay at Hotel.\n\n" +
                "DAY 3: Khajuraho to Orchha – Fort & Temple Tour\n" +
                "After breakfast, check out and proceed to Orchha (approx. 4 hours by road). On arrival, check in at the hotel and then begin exploring the majestic Orchha Fort Complex, including Jahangir Mahal, Raja Mahal, and Sheesh Mahal. Visit the beautiful Ram Raja Temple, the only temple in India where Lord Rama is worshipped as a king. Dinner & Overnight Stay at Hotel.\n\n" +
                "DAY 4: Orchha – Nature & Heritage Walks\n" +
                "After breakfast, explore Chaturbhuj Temple, Lakshmi Narayan Temple, and the peaceful Cenotaphs (Chhatris) by the Betwa River. Spend the rest of the day enjoying leisure walks along the riverside or opt for water activities such as river rafting (seasonal). You may also explore the local handicrafts market. Dinner & Overnight Stay at Hotel.\n\n" +
                "DAY 5: Orchha to Gwalior – Sightseeing\n" +
                "After breakfast, check out and drive to Gwalior (approx. 3.5 hours). Upon arrival, visit the iconic Gwalior Fort, one of the largest hill forts in India, known for its stunning views and historical significance. Explore Saas Bahu Temples, Teli ka Mandir, and the Man Singh Palace inside the fort complex. In the evening, enjoy the Sound and Light Show (subject to schedule). Dinner & Overnight Stay at Hotel.\n\n" +
                "DAY 6: Departure from Gwalior\n" +
                "After breakfast, check out from the hotel. You will be transferred to Gwalior Railway Station or Airport for your onward journey, carrying with your rich memories of Madhya Pradesh's architectural wonders and royal heritage.",
                "• 05 Night Hotel Accommodation on double sharing basis\n" +
                "• Pickup & drop from Railway station\n" +
                "• Morning tea\n" +
                "• Buffet Breakfast, Buffet Dinner\n" +
                "• One Sightseeing tour of Khajuraho By Pvt. Car\n" +
                "• 02 Sightseeing tour of Orchha By Pvt. Car\n" +
                "• One Sightseeing tour of Gwalior By Pvt. Car\n" +
                "• Our assistance at the time of arrival & departure\n" +
                "• Inclusive all taxes\n" +
                "• No any Hidden cost",
                "• Personal expenses including but not limited to laundry, porterage, telephone calls, tips, gratuities, bottled water, and beverages (both alcoholic and non-alcoholic).\n" +
                "• Charges for optional or adventure activities such as rafting, rock climbing, paragliding, and joy rides (e.g., toy train).\n" +
                "• Travel insurance, entrance fees at monuments or sightseeing spots, professional guide services, and snow vehicle charges (if applicable due to weather conditions).\n" +
                "• Additional expenses arising due to unforeseen events such as natural calamities (landslides, road blockages), political unrest, strikes, or other force majeure situations.\n" +
                "• Room heater charges at hotels or resorts.\n" +
                "• Any items or services not explicitly mentioned under the 'Inclusions' section."
        );
    }

    private void seedDetailedUjjainIndoreTour() {
        // Ujjain – Indore Tour Package
        seedDetailed("Ujjain – Indore Tour Package",
                "Madhya Pradesh",
                new BigDecimal("8999"),
                3,
                "/images/ujjain.jpg",
                Package.PackageType.DOMESTIC,
                "Experience the spiritual essence of Ujjain and the vibrant culture of Indore with our comprehensive 3 nights 4 days package. Visit sacred temples, historic palaces, and indulge in authentic Malwa cuisine in this divine journey through Madhya Pradesh.",
                "DAY 1: Arrival in Indore – Transfer to Ujjain – Temple Visit\n" +
                "Upon arrival at Indore Airport or Railway Station, you will be greeted by our representative and transferred to Ujjain (approx. 1.5 hours by road). Check in to your hotel and relax. In the evening, visit the sacred Mahakaleshwar Jyotirlinga Temple, one of the twelve Jyotirlingas of Lord Shiva. Experience the divine ambiance and attend the evening aarti on the banks of the Shipra River. Dinner & Overnight Stay at Hotel.\n\n" +
                "DAY 2: Full-Day Ujjain Sightseeing\n" +
                "Start your day early with an optional visit to attend the Bhasma Aarti at Mahakaleshwar (advance booking recommended). After breakfast, explore other important spiritual and cultural landmarks including Kal Bhairav Temple, Mangalnath Temple, Harsiddhi Temple, Ram Ghat, and Sandipani Ashram. Enjoy the peaceful environment of this holy city. Dinner & Overnight Stay at Hotel.\n\n" +
                "DAY 3: Ujjain to Indore – Local Sightseeing\n" +
                "After breakfast, check out and drive to Indore. Upon arrival, visit the famous Rajwada Palace, an iconic symbol of Holkar architecture, and explore Lal Bagh Palace and the Kanch Mandir (Glass Temple). In the evening, stroll through Sarafa Bazaar, a popular night street food market. Check-in at the hotel. Dinner & Overnight Stay at Hotel.\n\n" +
                "DAY 4: Departure from Indore\n" +
                "After breakfast, check out from your hotel. You will be dropped at Indore Airport or Railway Station for your return journey, carrying divine memories of Ujjain and the vibrant charm of Indore.",
                "• 03 Night Hotel Accommodation on double sharing basis\n" +
                "• Pickup & drop from Railway station\n" +
                "• Morning tea\n" +
                "• Buffet Breakfast, Buffet Dinner\n" +
                "• One Sightseeing tour of Indore By Pvt. Car\n" +
                "• 02 Sightseeing tour of Ujjain By Pvt. Car\n" +
                "• Our assistance at the time of arrival & departure\n" +
                "• Inclusive all taxes\n" +
                "• No any Hidden cost",
                "• Personal expenses including but not limited to laundry, porterage, telephone calls, tips, gratuities, bottled water, and beverages (both alcoholic and non-alcoholic).\n" +
                "• Charges for optional or adventure activities such as rafting, rock climbing, paragliding, and joy rides (e.g., toy train).\n" +
                "• Travel insurance, entrance fees at monuments or sightseeing spots, professional guide services, and snow vehicle charges (if applicable due to weather conditions).\n" +
                "• Additional expenses arising due to unforeseen events such as natural calamities (landslides, road blockages), political unrest, strikes, or other force majeure situations.\n" +
                "• Room heater charges at hotels or resorts.\n" +
                "• Any items or services not explicitly mentioned under the 'Inclusions' section."
        );
    }

    private void seedDetailedKanhaPackages() {
        // Wild Escape to Kanha: 2 Nights / 3 Days Jungle Adventure
        seedDetailed("Wild Escape to Kanha (2 Nights / 3 Days)",
                "Madhya Pradesh",
                new BigDecimal("8999"),
                2,
                "/images/Kanha-National-Park.jpg",
                Package.PackageType.DOMESTIC,
                "Experience the thrill of wildlife in one of India's premier tiger reserves with our 2 nights 3 days Kanha National Park package. Get up close with Royal Bengal Tigers, deer, and exotic birds in their natural habitat.",
                "DAY 1: Arrival at Kanha – Check-in and Jungle Ambience\n" +
                "Upon arrival at the nearest railway station (Gondia or Jabalpur), you will be received by our representative and transferred to your wildlife resort in Kanha National Park (approx. 3–4 hours' drive depending on the station). After check-in and freshening up, enjoy a relaxed afternoon soaking in the natural surroundings of the forest. Take a nature walk around the resort area or visit a nearby tribal village for a cultural glimpse. In the evening, enjoy a cozy bonfire (seasonal) and a wildlife documentary. Dinner & Overnight Stay at Hotel.\n\n" +
                "DAY 2: Kanha Jungle Safari Experience\n" +
                "Wake up early and head for an exciting morning jungle safari in an open gypsy, accompanied by an expert naturalist. Kanha is famous for its population of Royal Bengal Tigers, Barasingha (swamp deer), leopards, wild dogs, and numerous bird species. Return to the resort for breakfast and a leisurely midday break. In the afternoon, head for a second safari to explore a different zone of the park, maximizing your chances of wildlife sightings. After an adventurous day, return to the resort to relax. Dinner & Overnight Stay at Hotel.\n\n" +
                "DAY 3: Departure from Kanha\n" +
                "After a hearty breakfast, check out from the resort. You will be transferred back to Gondia or Jabalpur Railway Station for your onward journey, carrying unforgettable memories of your close encounters with the wild in the heart of Madhya Pradesh.",
                "• 02 Night Hotel Accommodation on double sharing basis\n" +
                "• Pickup & drop from Railway station\n" +
                "• Morning tea\n" +
                "• Buffet Breakfast, Buffet Dinner\n" +
                "• One Sightseeing of Kanha National Park Non-Forest area By Pvt. Car\n" +
                "• One Sightseeing tour of Kanha National Park Forest Area By Gypsy\n" +
                "• Our assistance at the time of arrival & departure\n" +
                "• Inclusive all taxes\n" +
                "• No any Hidden cost",
                "• Personal expenses including but not limited to laundry, porterage, telephone calls, tips, gratuities, bottled water, and beverages (both alcoholic and non-alcoholic).\n" +
                "• Charges for optional or adventure activities such as rafting, rock climbing, paragliding, and joy rides (e.g., toy train).\n" +
                "• Travel insurance, entrance fees at monuments or sightseeing spots, professional guide services, and snow vehicle charges (if applicable due to weather conditions).\n" +
                "• Additional expenses arising due to unforeseen events such as natural calamities (landslides, road blockages), political unrest, strikes, or other force majeure situations—such costs must be borne directly by the traveler at the time.\n" +
                "• Room heater charges at hotels or resorts.\n" +
                "• Any items or services not explicitly mentioned under the 'Inclusions' section."
        );

        // Kanha (02 Nights), Jabalpur (01 Night)
        seedDetailed("Kanha (02 Nights), Jabalpur (01 Night)",
                "Madhya Pradesh",
                new BigDecimal("11999"),
                3,
                "/images/Jabalpur.jpg",
                Package.PackageType.DOMESTIC,
                "Combine the thrill of wildlife safari in Kanha National Park with the scenic beauty of Jabalpur's marble rocks and waterfalls. This 3 nights 4 days package offers the perfect blend of adventure and nature.",
                "DAY 1: Arrival at Jabalpur – Transfer to Kanha National Park\n" +
                "Upon arrival at Jabalpur Railway Station or Airport, you will be received by our representative and driven to Kanha National Park (approx. 4–5 hours by road), one of India's finest tiger reserves and a paradise for wildlife lovers. After check-in at the jungle resort and some relaxation, spend the evening enjoying the serene forest atmosphere or an optional village walk. Dinner & Overnight Stay at Hotel.\n\n" +
                "DAY 2: Kanha – Jungle Safari and Leisure\n" +
                "Rise early for a thrilling morning jungle safari in Kanha's core zone with a forest guide and gypsy (subject to availability and pre-booking). Witness the pristine beauty of the park and its rich wildlife, including tigers, barasinghas, leopards, wild dogs, and a variety of birds. Return to the resort for breakfast and leisure time. Later, you may opt for another evening safari or enjoy nature trails and birdwatching around the resort. Dinner & Overnight Stay at Hotel.\n\n" +
                "DAY 3: Kanha to Jabalpur – Sightseeing in Jabalpur\n" +
                "After breakfast, check out from the resort and drive back to Jabalpur. Upon arrival, check-in at your hotel. In the afternoon, explore key attractions such as the famous Marble Rocks at Bhedaghat, the majestic Dhuandhar Waterfall, and take a boat ride on the Narmada River (subject to water level and availability). You can also visit Chausath Yogini Temple for panoramic views. Return to the hotel. Dinner & Overnight Stay at Hotel.\n\n" +
                "DAY 4: Departure from Jabalpur\n" +
                "After breakfast, check out from the hotel. You will be transferred to Jabalpur Railway Station or Airport for your onward journey, carrying unforgettable memories of Kanha's wilderness and Jabalpur's natural beauty.",
                "• 03 Night Hotel Accommodation on double sharing basis\n" +
                "• Pickup & drop from Railway station\n" +
                "• Morning tea\n" +
                "• Buffet Breakfast, Buffet Dinner\n" +
                "• One Sightseeing of Kanha National Park Non-Forest area By Pvt. Car\n" +
                "• One Sightseeing tour of Kanha National Park Forest Area By Gypsy\n" +
                "• One Local Sightseeing tour of Jabalpur By Pvt. Car\n" +
                "• Our assistance at the time of arrival & departure\n" +
                "• Inclusive all taxes\n" +
                "• No any Hidden cost",
                "• Personal expenses including but not limited to laundry, porterage, telephone calls, tips, gratuities, bottled water, and beverages (both alcoholic and non-alcoholic).\n" +
                "• Charges for optional or adventure activities such as rafting, rock climbing, paragliding, and joy rides (e.g., toy train).\n" +
                "• Travel insurance, entrance fees at monuments or sightseeing spots, professional guide services, and snow vehicle charges (if applicable due to weather conditions).\n" +
                "• Additional expenses arising due to unforeseen events such as natural calamities (landslides, road blockages), political unrest, strikes, or other force majeure situations—such costs must be borne directly by the traveler at the time.\n" +
                "• Room heater charges at hotels or resorts.\n" +
                "• Any items or services not explicitly mentioned under the 'Inclusions' section."
        );
    }

    private void seedDetailedBandhavgarhPackage() {
        // Bandhavgarh Tour for 02 Nights / 03 Days
        seedDetailed("Bandhavgarh Tour (02 Nights / 03 Days)",
                "Madhya Pradesh",
                new BigDecimal("7999"),
                2,
                "/images/Bandhavgarh.jpg",
                Package.PackageType.DOMESTIC,
                "Experience the thrill of tiger spotting in Bandhavgarh National Park, one of India's most famous wildlife destinations. This 2 nights 3 days package offers excellent chances to see Royal Bengal Tigers in their natural habitat.",
                "DAY 1: Arrival at Bandhavgarh – Check-in & Leisure\n" +
                "Arrive at Umaria Railway Station or directly at Bandhavgarh, where our representative will receive you. Transfer to the wildlife resort and check in. After lunch and some relaxation, you may explore the nearby nature trails or visit Bandhavgarh Fort (if accessible). Enjoy the evening at leisure amidst forest surroundings. Dinner and overnight stay at the resort.\n\n" +
                "DAY 2: Jungle Safari and Nature Activities\n" +
                "Early morning, head out for an exciting jungle safari in an open gypsy with a trained naturalist (subject to permit availability). Explore the core area of the park and spot majestic wildlife such as tigers, leopards, deer, langurs, and a variety of bird species. Return to the resort for breakfast. Spend the day at leisure or engage in resort-based activities. Optional: You may opt for an evening safari at additional cost. Dinner and overnight stay at the resort.\n\n" +
                "DAY 3: Check-out and Departure\n" +
                "After breakfast, check out from the resort. You will be dropped at Umaria Railway Station or your onward travel point, carrying with your unforgettable memories of the jungle.",
                "• 02 Night Hotel Accommodation on double sharing basis\n" +
                "• Pickup & drop from Railway station\n" +
                "• Morning tea\n" +
                "• Buffet Breakfast, Buffet Dinner\n" +
                "• One Sightseeing of Bandhavgarh National Park Forest area By Gypsy\n" +
                "• Our assistance at the time of arrival & departure\n" +
                "• Inclusive all taxes\n" +
                "• No any Hidden cost",
                "• Personal expenses including but not limited to laundry, porterage, telephone calls, tips, gratuities, bottled water, and beverages (both alcoholic and non-alcoholic).\n" +
                "• Charges for optional or adventure activities such as rafting, rock climbing, paragliding, and joy rides (e.g., toy train).\n" +
                "• Travel insurance, entrance fees at monuments or sightseeing spots, professional guide services, and snow vehicle charges (if applicable due to weather conditions).\n" +
                "• Additional expenses arising due to unforeseen events such as natural calamities (landslides, road blockages), political unrest, strikes, or other force majeure situations—such costs must be borne directly by the traveler at the time.\n" +
                "• Room heater charges at hotels or resorts.\n" +
                "• Any items or services not explicitly mentioned under the 'Inclusions' section."
        );
    }

    private void seedDetailedGoldenTriangle() {
        seedDetailed(
            "Golden Triangle (5 Night/6 Days)",
            "Delhi-Agra-Jaipur",
            new BigDecimal("15999"),
            6,
            "/images/golden-triangle.jpg",
            Package.PackageType.DOMESTIC,
            "Experience India's Golden Triangle with our comprehensive 5 nights 6 days package covering Delhi, Agra, and Jaipur. Visit iconic monuments, forts, and palaces that showcase India's rich heritage and architectural marvels.",
            "DAY 1: Arrival Delhi – Local Sightseeing\n" +
            "Arrive in Delhi and check into your hotel. After freshening up, visit India Gate, Rashtrapati Bhavan, and Connaught Place. Evening at leisure for shopping or relaxation.\n\n" +
            "DAY 2: Delhi – Agra (210 km / 4 hours)\n" +
            "After breakfast, drive to Agra. Visit the magnificent Taj Mahal, Agra Fort, and Itimad-ud-Daulah. Overnight in Agra.\n\n" +
            "DAY 3: Agra – Jaipur (240 km / 5 hours)\n" +
            "Morning visit to Taj Mahal at sunrise. Drive to Jaipur, visiting Fatehpur Sikri en route. Evening at leisure in Jaipur.\n\n" +
            "DAY 4: Jaipur Sightseeing\n" +
            "Full day sightseeing including Amber Fort, City Palace, Jantar Mantar, and Hawa Mahal. Evening free for shopping.\n\n" +
            "DAY 5: Jaipur – Delhi (260 km / 5 hours)\n" +
            "Morning at leisure or optional shopping. Drive back to Delhi for overnight stay.\n\n" +
            "DAY 6: Delhi Departure\n" +
            "After breakfast, transfer to airport for departure.",
            "• 05 Night Hotel Accommodation on double sharing basis\n" +
            "• Daily Breakfast\n" +
            "• All transfers and sightseeing by private AC vehicle\n" +
            "• Monument entrance fees\n" +
            "• Professional guide services\n" +
            "• All taxes included",
            "• Any airfare/train fare\n" +
            "• Meals other than those specified\n" +
            "• Personal expenses such as laundry, telephone calls, tips, etc.\n" +
            "• Any other items not mentioned in 'Package Includes'"
        );
    }

    private void seedDetailedGangtokDarjeeling() {
        seedDetailed("Majestic Gangtok Darjeeling (04 Days)",
                "Sikkim/West Bengal",
                new BigDecimal("15999"),
                4,
                "/images/gangtok-darjeeling.jpg",
                Package.PackageType.DOMESTIC,
                "Discover the enchanting beauty of East India with our 4-day Gangtok and Darjeeling package. Experience breathtaking mountain views, Buddhist monasteries, and colonial charm in this Himalayan paradise.",
                "DAY 1: Arrival Gangtok – Local Sightseeing\n" +
                "Arrive at Bagdogra Airport and transfer to Gangtok. Check into hotel and visit Enchey Monastery, Flower Show, and Gangtok Market.\n\n" +
                "DAY 2: Gangtok Full Day Sightseeing\n" +
                "Visit Tsomgo Lake, Baba Mandir, and Nathula Pass (subject to permit). Evening at leisure.\n\n" +
                "DAY 3: Gangtok – Darjeeling (110 km / 4 hours)\n" +
                "Drive to Darjeeling. Visit Himalayan Mountaineering Institute, PNZ Zoological Park, and Tibetan Refugee Centre.\n\n" +
                "DAY 4: Darjeeling Sightseeing & Departure\n" +
                "Early morning visit to Tiger Hill for sunrise view of Kanchenjunga. Visit Batasia Loop and Darjeeling Railway Station. Transfer to Bagdogra Airport.",
                "• 03 Night Hotel Accommodation\n" +
                "• Daily Breakfast and Dinner\n" +
                "• All transfers and sightseeing\n" +
                "• Professional driver\n" +
                "• All permits and taxes",
                "• Personal expenses, optional activities\n" +
                "• Entry fees not mentioned\n" +
                "• Travel insurance"
        );
    }

    private void seedDetailedManaliShimla() {
        seedDetailed("Manali –Shimla Tour (5 Night / 6 Days)",
                "Himachal Pradesh",
                new BigDecimal("15999"),
                6,
                "/images/manali.jpg",
                Package.PackageType.DOMESTIC,
                "Experience the best of Himachal Pradesh with our comprehensive Manali-Shimla tour. From Shimla's colonial charm to Manali's adventure activities, this package offers the perfect Himalayan getaway.",
                "DAY 1: Arrival Shimla\n" +
                "Arrive at Chandigarh and transfer to Shimla. Evening at leisure.\n\n" +
                "DAY 2: Shimla Sightseeing\n" +
                "Visit Kufri, Mall Road, Christ Church, and Scandal Point.\n\n" +
                "DAY 3: Shimla – Manali (250 km / 7 hours)\n" +
                "Scenic drive to Manali via Kullu Valley.\n\n" +
                "DAY 4: Manali Sightseeing\n" +
                "Visit Hadimba Temple, Vashisht Hot Springs, and Mall Road.\n\n" +
                "DAY 5: Manali Local Activities\n" +
                "Visit Solang Valley or Rohtang Pass (seasonal). Evening at leisure.\n\n" +
                "DAY 6: Manali – Chandigarh Departure\n" +
                "Transfer to Chandigarh airport for departure.",
                "• 05 Night Hotel Accommodation\n" +
                "• Daily Breakfast and Dinner\n" +
                "• All transfers by private vehicle\n" +
                "• Professional driver\n" +
                "• All taxes included",
                "• Personal expenses, adventure activities\n" +
                "• Entry fees, travel insurance"
        );
    }

    private void seedDetailedSrinagarLeh() {
        seedDetailed("Srinagar –Leh Tour Package (9 Night / 10 Days)",
                "J&K/Ladakh",
                new BigDecimal("45000"),
                10,
                "/images/srinagar-leh.jpg",
                Package.PackageType.DOMESTIC,
                "Embark on an unforgettable journey through the majestic landscapes of Ladakh with our comprehensive 9 nights 10 days Srinagar-Leh package. Experience the unique culture, monasteries, and breathtaking high-altitude deserts.",
                "DAY 1: Arrival Srinagar\n" +
                "Arrive at Srinagar Airport and transfer to houseboat. Evening Shikara ride on Dal Lake.\n\n" +
                "DAY 2: Srinagar Local Sightseeing\n" +
                "Visit Mughal Gardens, Shalimar Bagh, Nishat Bagh, and Pari Mahal.\n\n" +
                "DAY 3: Srinagar – Kargil (200 km / 6 hours)\n" +
                "Drive to Kargil via Sonamarg. Visit Drass War Memorial.\n\n" +
                "DAY 4: Kargil – Leh (220 km / 5 hours)\n" +
                "Visit Lamayuru Monastery, drive to Leh via Alchi and Likir.\n\n" +
                "DAY 5: Leh Local Sightseeing\n" +
                "Visit Shanti Stupa, Leh Palace, and local market.\n\n" +
                "DAY 6: Leh – Nubra Valley (160 km / 5 hours)\n" +
                "Drive to Nubra Valley via Khardung La. Camel safari at Hunder.\n\n" +
                "DAY 7: Nubra Valley – Pangong Lake (140 km / 6 hours)\n" +
                "Visit Diskit Monastery, drive to Pangong Lake.\n\n" +
                "DAY 8: Pangong Lake – Leh (140 km / 5 hours)\n" +
                "Morning at Pangong Lake, return to Leh.\n\n" +
                "DAY 9: Leh Local Activities\n" +
                "Visit Thiksey Monastery, Shey Palace, and Stok Palace.\n\n" +
                "DAY 10: Leh Departure\n" +
                "Transfer to Leh airport for departure.",
                "• 09 Night Hotel/Houseboat/Camp Accommodation\n" +
                "• Daily Breakfast and Dinner\n" +
                "• All transfers by private vehicle\n" +
                "• Inner Line Permits\n" +
                "• Professional driver\n" +
                "• All taxes included",
                "• Personal expenses, adventure activities\n" +
                "• Entry fees, travel insurance\n" +
                "• Extra costs due to weather conditions"
        );
    }

    private void seedDetailedAndaman() {
        seedDetailed("Andaman Package (5 Nights/6 Days)",
                "Andaman & Nicobar",
                new BigDecimal("15999"),
                6,
                "/images/andaman.jpg",
                Package.PackageType.DOMESTIC,
                "Discover the pristine beaches and marine life of Andaman with our comprehensive 5 nights 6 days package. Experience crystal clear waters, adventure activities, and tropical paradise.",
                "DAY 1: Arrival Port Blair\n" +
                "Arrive at Port Blair Airport and transfer to hotel. Visit Cellular Jail and Light & Sound Show.\n\n" +
                "DAY 2: Port Blair – Ross Island\n" +
                "Visit Ross Island, North Bay Island, and enjoy water activities.\n\n" +
                "DAY 3: Port Blair – Havelock Island\n" +
                "Ferry to Havelock Island. Visit Radhanagar Beach and enjoy beach activities.\n\n" +
                "DAY 4: Havelock Island\n" +
                "Day at leisure or optional activities like snorkeling, scuba diving.\n\n" +
                "DAY 5: Havelock – Port Blair\n" +
                "Return to Port Blair. Visit local markets and shopping.\n\n" +
                "DAY 6: Port Blair Departure\n" +
                "Transfer to airport for departure.",
                "• 05 Night Hotel Accommodation\n" +
                "• Daily Breakfast\n" +
                "• All transfers and ferry tickets\n" +
                "• Entry tickets to monuments\n" +
                "• All taxes included",
                "• Personal expenses, water sports\n" +
                "• Meals not mentioned\n" +
                "• Travel insurance"
        );
    }

    private void seedDetailedKerala() {
        seedDetailed("Kerala 5N/6D Highlights",
                "Kerala",
                new BigDecimal("15500"),
                6,
                "/images/kerala.jpg",
                Package.PackageType.DOMESTIC,
                "Experience the backwaters, beaches, and hill stations of God's Own Country with our comprehensive Kerala package. Discover the diverse landscapes and rich culture of Kerala.",
                "DAY 1: Arrival Cochin – Munnar (130 km / 4 hours)\n" +
                "Arrive at Cochin Airport and drive to Munnar. Check into resort.\n\n" +
                "DAY 2: Munnar Sightseeing\n" +
                "Visit tea plantations, Mattupetty Dam, Echo Point, and Kundala Lake.\n\n" +
                "DAY 3: Munnar – Thekkady (110 km / 3 hours)\n" +
                "Drive to Thekkady. Evening boat safari at Periyar Lake.\n\n" +
                "DAY 4: Thekkady – Alleppey (140 km / 4 hours)\n" +
                "Drive to Alleppey. Check into houseboat for backwater cruise.\n\n" +
                "DAY 5: Alleppey – Kovalam (150 km / 4 hours)\n" +
                "Drive to Kovalam. Relax on the beach.\n\n" +
                "DAY 6: Kovalam – Trivandrum Departure\n" +
                "Morning at leisure, transfer to Trivandrum Airport.",
                "• 05 Night Hotel/Houseboat Accommodation\n" +
                "• Daily Breakfast and Dinner\n" +
                "• Houseboat cruise with meals\n" +
                "• All transfers by private vehicle\n" +
                "• All taxes included",
                "• Personal expenses, optional activities\n" +
                "• Entry fees not mentioned\n" +
                "• Travel insurance"
        );
    }

    private void seedDetailedGoa() {
        seedDetailed("Luxury Holiday in Goa 2N/3D",
                "Goa",
                new BigDecimal("6999"),
                3,
                "/images/goa.jpg",
                Package.PackageType.DOMESTIC,
                "Experience the sun, sand, and sea of Goa with our luxury holiday package. Relax on pristine beaches, explore historic sites, and enjoy the vibrant nightlife.",
                "DAY 1: Arrival Goa\n" +
                "Arrive at Goa Airport and transfer to hotel. Evening at leisure.\n\n" +
                "DAY 2: North Goa Sightseeing\n" +
                "Visit Fort Aguada, Calangute Beach, Baga Beach, and Anjuna Beach.\n\n" +
                "DAY 3: South Goa Sightseeing & Departure\n" +
                "Visit Colva Beach, Basilica of Bom Jesus, and Se Cathedral. Transfer to airport.",
                "• 02 Night Hotel Accommodation\n" +
                "• Daily Breakfast\n" +
                "• All transfers by private vehicle\n" +
                "• Sightseeing tours\n" +
                "• All taxes included",
                "• Personal expenses, water sports\n" +
                "• Meals not mentioned\n" +
                "• Travel insurance"
        );
    }

    private void seed(String name, String destination, BigDecimal price, int duration, String imageUrl, Package.PackageType type) {
        Package p = new Package();
        p.setName(name);
        p.setDescription(name + " package by HappyGhumakkads. Contact us for a customized itinerary.");
        p.setDestination(destination);
        p.setPrice(price);
        p.setDuration(duration);
        p.setImageUrl(imageUrl);
        p.setType(type);
        p.setInclusions("Hotel, Breakfast, Transfers, Sightseeing as per itinerary");
        p.setExclusions("Flights, Personal expenses, GST, Anything not mentioned in inclusions");
        packageService.savePackage(p);
    }

    private void seedDetailed(String name, String destination, BigDecimal price, int duration, String imageUrl,
                             Package.PackageType type, String description, String itinerary, String inclusions, String exclusions) {
        Package p = new Package();
        p.setName(name);
        p.setDescription(description);
        p.setDestination(destination);
        p.setPrice(price);
        p.setDuration(duration);
        p.setImageUrl(imageUrl);
        p.setType(type);
        p.setItinerary(itinerary);
        p.setInclusions(inclusions);
        p.setExclusions(exclusions);
        packageService.savePackage(p);
    }
}
