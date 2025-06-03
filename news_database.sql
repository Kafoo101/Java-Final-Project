-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 03, 2025 at 04:41 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `news_database`
--

-- --------------------------------------------------------

--
-- Table structure for table `category`
--

CREATE TABLE `category` (
  `No` int(11) NOT NULL,
  `Name` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `category`
--

INSERT INTO `category` (`No`, `Name`) VALUES
(1, 'politics'),
(2, 'economy'),
(3, 'environment'),
(4, 'entertainment'),
(5, 'social'),
(6, 'technology'),
(7, 'sports'),
(8, 'health'),
(9, 'science'),
(10, 'worldnews');

-- --------------------------------------------------------

--
-- Table structure for table `economy`
--

CREATE TABLE `economy` (
  `NewsID` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `economy`
--

INSERT INTO `economy` (`NewsID`) VALUES
('N-003'),
('N-004'),
('N-007'),
('N-009'),
('N-012'),
('N-013'),
('N-022'),
('N-030'),
('N-041'),
('N-044'),
('N-047');

-- --------------------------------------------------------

--
-- Table structure for table `entertainment`
--

CREATE TABLE `entertainment` (
  `NewsID` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `entertainment`
--

INSERT INTO `entertainment` (`NewsID`) VALUES
('N-024'),
('N-038'),
('N-039'),
('N-043'),
('N-044'),
('N-046');

-- --------------------------------------------------------

--
-- Table structure for table `environment`
--

CREATE TABLE `environment` (
  `NewsID` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `environment`
--

INSERT INTO `environment` (`NewsID`) VALUES
('N-014'),
('N-027'),
('N-028'),
('N-029'),
('N-031'),
('N-032'),
('N-033'),
('N-034'),
('N-035'),
('N-039');

-- --------------------------------------------------------

--
-- Table structure for table `health`
--

CREATE TABLE `health` (
  `NewsID` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `health`
--

INSERT INTO `health` (`NewsID`) VALUES
('N-002'),
('N-007'),
('N-011'),
('N-012'),
('N-022'),
('N-023'),
('N-026'),
('N-036'),
('N-037'),
('N-038'),
('N-043'),
('N-044'),
('N-045'),
('N-046'),
('N-047');

-- --------------------------------------------------------

--
-- Table structure for table `news`
--

CREATE TABLE `news` (
  `NewsID` varchar(20) NOT NULL,
  `Title` varchar(200) NOT NULL,
  `URL` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `news`
--

INSERT INTO `news` (`NewsID`, `Title`, `URL`) VALUES
('N-001', 'Sycamore Gap: Two men convicted of felling one of UK’s most famous trees in September 2023 ', 'https://edition.cnn.com/2025/05/09/uk/sycamore-gap-trees-convictions-scli-intl'),
('N-002', 'New Orleans jail escape: Timeline of how 10 inmates used skill and luck to break free ', 'https://edition.cnn.com/us/new-orleans-jail-escape-timeline-dg'),
('N-003', 'Tariffs, and Trump’s entire economic agenda, were just thrown into chaos | CNN Business', 'https://edition.cnn.com/2025/05/29/business/tariffs-trump-economic-plan-hnk'),
('N-004', 'How the big, beautiful and expensive bill could cost you | CNN Business', 'https://edition.cnn.com/2025/05/22/economy/trump-tax-bill-debt-deficit'),
('N-005', 'Damage to ancient Hadrian’s Wall confirmed after famous UK tree ‘deliberately felled’ ', 'https://edition.cnn.com/travel/damage-to-ancient-hadrians-wall-confirmed-after-famous-uk-tree-deliberately-felled/index.html'),
('N-006', 'How the New Orleans jail inmate escape unfolded ', 'https://edition.cnn.com/2025/05/18/us/new-orleans-louisiana-inmates-escape'),
('N-007', 'Rarity over perfection: Why jewelers are championing ‘ugly’ gems ', 'https://edition.cnn.com/2025/05/29/style/imperfect-gem-stones-jewelry-trend'),
('N-008', 'Carlos Alcaraz leads French Open crowd in song after reaching the third round ', 'https://edition.cnn.com/2025/05/29/sport/carlos-alcaraz-fabian-marozsan-french-open-spt-intl'),
('N-009', 'S&P 500 posts best month since 2023 as Wall Street tries to ignore the trade war | CNN Business', 'https://edition.cnn.com/2025/05/30/investing/us-stock-market'),
('N-010', 'Archaeologists found a strange red mark on rock. Forensic police helped them unravel who made it ', 'https://edition.cnn.com/2025/05/29/science/neanderthal-complete-fingerprint-stone-art-scli-intl'),
('N-011', 'What it’s like planning vacations for the rich and famous in Southeast Asia ', 'https://edition.cnn.com/travel/thailand-vacations-rich-famous-smiling-albino'),
('N-012', 'Canadians are boycotting the US. Are American travelers still welcome in Canada? ', 'https://edition.cnn.com/2025/05/27/travel/american-travelers-welcome-in-canada'),
('N-013', 'Fengyang Drum Tower: Tourists scramble as roof tiles fall off 600-year-old Chinese structure ', 'https://edition.cnn.com/2025/05/21/travel/china-fengyang-drum-tower-tile-falling-intl-hnk'),
('N-014', 'Europe’s hottest city battles to keep its cool as tourists arrive for another scorching summer ', 'https://edition.cnn.com/2025/05/28/travel/athens-heat-greece-tourism-summer'),
('N-015', 'Discovery of two gold rings reveal Greek influence in ancient Jerusalem ', 'https://edition.cnn.com/2025/05/29/science/jerusalem-ancient-gold-rings'),
('N-016', 'Fossil teeth analysis upends what’s known about megalodon’s diet, scientists say ', 'https://edition.cnn.com/2025/05/27/science/megalodon-diet-prey-fossil-teeth'),
('N-017', 'British canoeist says he’s being forced to choose between Olympic dreams and OnlyFans ', 'https://edition.cnn.com/2025/05/30/sport/british-canoeist-banned-onlyfans-gbr-scli-intl'),
('N-018', 'Russian captain involved in US tanker crash pleads not guilty to manslaughter in UK court ', 'https://edition.cnn.com/2025/05/30/uk/russian-captain-us-tanker-crash-denies-manslaughter-uk'),
('N-019', 'This robotic arm is creating traditional Chinese ink paintings ', 'https://edition.cnn.com/style/victor-wong-ai-ink-painting-hong-kong-hnk-spc-intl'),
('N-020', 'This fragrance company is trying to recreate the scent of extinct blooms ', 'https://edition.cnn.com/2025/05/30/style/future-society-fragrance-extinct-flowers'),
('N-021', 'Gaza Humanitarian Foundation isn’t screening recipients — despite being established to keep supplies from Hamas ', 'https://edition.cnn.com/2025/05/30/middleeast/gaza-humanitarian-foundation-screenings-hamas-intl'),
('N-022', 'Taylor Swift now owns her entire catalog of music ', 'https://edition.cnn.com/2025/05/30/entertainment/taylor-swift-owns-catalog-masters-music'),
('N-023', '‘Diddy’ trial takeaways: Defense questions ex-employee on why she kept working for Sean Combs despite alleged abuse ', 'https://edition.cnn.com/2025/05/30/entertainment/takeaways-sean-diddy-combs-trial-day13'),
('N-024', 'Suge Knight urges longtime rival Sean ‘Diddy’ Combs to take the stand to ‘humanize’ himself ', 'https://edition.cnn.com/2025/05/30/entertainment/suge-knight-sean-diddy-combs-trial-intl-hnk'),
('N-025', 'Todd Chrisley ‘blessed’ and vows to fight for others in the justice system ', 'https://edition.cnn.com/2025/05/30/entertainment/todd-chrisley-release-press-conference'),
('N-026', 'Covid-19 shots for kids remain on CDC vaccine schedule with slightly different designation ', 'https://edition.cnn.com/2025/05/30/health/children-covid-vaccine-cdc'),
('N-027', 'Smoke pours into the US as Canada wildfires force province’s largest evacuation in ‘living memory’ ', 'https://edition.cnn.com/2025/05/29/weather/canada-wildfires-smoke-midwest-climate'),
('N-028', 'Rare May nor’easter will bring the gloom leading into Memorial Day Weekend ', 'https://edition.cnn.com/2025/05/21/weather/may-noreaster-memorial-day-weekend-climate'),
('N-029', 'Unexpected tornado leaves at least 1 dead, multiple injured in Kentucky ', 'https://edition.cnn.com/2025/05/30/weather/tornado-kentucky-severe-storm-climate'),
('N-030', 'NOAA hurricane outlook shows active season is looms as agencies struggle to prepare ', 'https://edition.cnn.com/2025/05/22/weather/noaa-hurricane-outlook-trump-fema'),
('N-031', 'Dangerous storms slam 10 states, unleashing multiple tornadoes and damaging winds ', 'https://edition.cnn.com/2025/05/19/weather/tornadoes-severe-storms-oklahoma-plains-climate'),
('N-032', 'An El Niño-less summer is coming. Here’s what that could mean for the US ', 'https://edition.cnn.com/2024/04/29/weather/la-nina-summer-forecast-us-el-nino-climate'),
('N-033', 'West wildfire risk is growing because of a plant that grows everywhere ', 'https://edition.cnn.com/2024/03/21/climate/wildfire-grass-risk-west-us'),
('N-034', 'Ancient Murujuga rock art under threat as Australia gives ‘proposed’ approval to Woodside gas plant extension ', 'https://edition.cnn.com/2025/05/28/climate/australia-murujuga-rock-art-woodside-climate-intl-hnk'),
('N-035', 'This company says its technology can help save the world. It’s now cutting 20% of its staff as Trump slashes climate funding ', 'https://edition.cnn.com/2025/05/30/climate/climeworks-pollution-carbon-capture-layoffs'),
('N-036', 'At V&A East Storehouse, the objects are yours to touch ', 'https://edition.cnn.com/2025/05/28/style/va-east-storehouse-museum-london'),
('N-037', 'Rebecca Black dresses for a shotgun wedding at the AMAs in Vegas ', 'https://edition.cnn.com/2025/05/26/style/rebecca-black-amas-2025-lotw'),
('N-038', '‘Fun, crazy and ludicrous’ images of Cannes before camera phones ', 'https://edition.cnn.com/2025/05/16/style/cannes-film-festival-derek-ridgers'),
('N-039', 'National Geographic Traveller competition winning photos ', 'https://edition.cnn.com/2025/05/29/travel/national-geographic-traveller-photo-competition-intl-scli'),
('N-040', '‘Napalm Girl’: World Press Photo ‘suspends’ attribution for iconic Vietnam War image ', 'https://edition.cnn.com/2025/05/19/style/napalm-girl-world-press-photo-nick-ut-hnk-intl'),
('N-041', 'How this Boston physics student became one of Murano’s youngest master glassmakers ', 'https://edition.cnn.com/style/murano-glass-roberto-beltrami-hnk-spc'),
('N-042', 'Jim Morrison’s stolen grave bust found after 37 years ', 'https://edition.cnn.com/2025/05/20/style/jim-morrison-grave-bust-found-intl-scli'),
('N-043', 'Inside Amoako Boafo’s rise to art-world stardom ', 'https://edition.cnn.com/2025/04/10/style/artist-amoako-boafo-gagosian-show'),
('N-044', 'After buzzy campaign, theater-less Mississippi town that inspired ‘Sinners’ will finally get to see the film ', 'https://edition.cnn.com/2025/05/10/entertainment/clarksdale-mississippi-mayor-excited-sinners-screening'),
('N-045', '“American Psycho” at 25: Patrick Bateman’s influence is everywhere online ', 'https://edition.cnn.com/2025/04/15/style/american-psycho-morning-routine-25-years'),
('N-046', 'Fake tan, fancy nails: How marathons became a catwalk for beauty ', 'https://edition.cnn.com/2025/04/23/style/runners-marathon-beauty'),
('N-047', 'Flat shoes are now allowed at the Cannes Film Festival. Can they rise to the occasion? ', 'https://edition.cnn.com/2025/05/24/style/cannes-flat-shoes-red-carpet');

-- --------------------------------------------------------

--
-- Table structure for table `politics`
--

CREATE TABLE `politics` (
  `NewsID` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `politics`
--

INSERT INTO `politics` (`NewsID`) VALUES
('N-001'),
('N-005'),
('N-017'),
('N-023'),
('N-024'),
('N-025'),
('N-026'),
('N-030'),
('N-040'),
('N-042');

-- --------------------------------------------------------

--
-- Table structure for table `science`
--

CREATE TABLE `science` (
  `NewsID` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `science`
--

INSERT INTO `science` (`NewsID`) VALUES
('N-005'),
('N-007'),
('N-010'),
('N-015'),
('N-016'),
('N-019'),
('N-020'),
('N-036'),
('N-038'),
('N-039'),
('N-040'),
('N-041'),
('N-047');

-- --------------------------------------------------------

--
-- Table structure for table `social`
--

CREATE TABLE `social` (
  `NewsID` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `social`
--

INSERT INTO `social` (`NewsID`) VALUES
('N-002'),
('N-006'),
('N-011'),
('N-013'),
('N-017'),
('N-018'),
('N-022'),
('N-023'),
('N-024'),
('N-025'),
('N-037'),
('N-042'),
('N-043'),
('N-044'),
('N-045');

-- --------------------------------------------------------

--
-- Table structure for table `sports`
--

CREATE TABLE `sports` (
  `NewsID` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `sports`
--

INSERT INTO `sports` (`NewsID`) VALUES
('N-008'),
('N-017'),
('N-018'),
('N-024'),
('N-037');

-- --------------------------------------------------------

--
-- Table structure for table `technology`
--

CREATE TABLE `technology` (
  `NewsID` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `technology`
--

INSERT INTO `technology` (`NewsID`) VALUES
('N-002'),
('N-019'),
('N-036'),
('N-041');

-- --------------------------------------------------------

--
-- Table structure for table `worldnews`
--

CREATE TABLE `worldnews` (
  `NewsID` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `worldnews`
--

INSERT INTO `worldnews` (`NewsID`) VALUES
('N-001'),
('N-005'),
('N-012'),
('N-013'),
('N-018'),
('N-021'),
('N-026'),
('N-027'),
('N-029'),
('N-030'),
('N-031'),
('N-040'),
('N-042');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`No`);

--
-- Indexes for table `economy`
--
ALTER TABLE `economy`
  ADD PRIMARY KEY (`NewsID`);

--
-- Indexes for table `entertainment`
--
ALTER TABLE `entertainment`
  ADD PRIMARY KEY (`NewsID`);

--
-- Indexes for table `environment`
--
ALTER TABLE `environment`
  ADD PRIMARY KEY (`NewsID`);

--
-- Indexes for table `health`
--
ALTER TABLE `health`
  ADD PRIMARY KEY (`NewsID`);

--
-- Indexes for table `news`
--
ALTER TABLE `news`
  ADD PRIMARY KEY (`NewsID`);

--
-- Indexes for table `politics`
--
ALTER TABLE `politics`
  ADD PRIMARY KEY (`NewsID`);

--
-- Indexes for table `science`
--
ALTER TABLE `science`
  ADD PRIMARY KEY (`NewsID`);

--
-- Indexes for table `social`
--
ALTER TABLE `social`
  ADD PRIMARY KEY (`NewsID`);

--
-- Indexes for table `sports`
--
ALTER TABLE `sports`
  ADD PRIMARY KEY (`NewsID`);

--
-- Indexes for table `technology`
--
ALTER TABLE `technology`
  ADD PRIMARY KEY (`NewsID`);

--
-- Indexes for table `worldnews`
--
ALTER TABLE `worldnews`
  ADD PRIMARY KEY (`NewsID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `category`
--
ALTER TABLE `category`
  MODIFY `No` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
