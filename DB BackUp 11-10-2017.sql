-- phpMyAdmin SQL Dump
-- version 4.2.12deb2+deb8u2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Oct 11, 2017 at 08:55 AM
-- Server version: 5.5.55-0+deb8u1
-- PHP Version: 5.6.30-0+deb8u1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `isa`
--

-- --------------------------------------------------------

--
-- Table structure for table `approvedproject`
--

CREATE TABLE IF NOT EXISTS `approvedproject` (
`approvedprojectID` smallint(6) NOT NULL,
  `userID` int(11) NOT NULL,
  `projectID` int(11) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `approvedproject`
--

INSERT INTO `approvedproject` (`approvedprojectID`, `userID`, `projectID`) VALUES
(2, 1, 14),
(3, 1, 13);

-- --------------------------------------------------------

--
-- Table structure for table `Book`
--

CREATE TABLE IF NOT EXISTS `Book` (
  `BookNo` int(11) NOT NULL,
  `Author` varchar(20) DEFAULT NULL,
  `Title` varchar(40) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `Book`
--

INSERT INTO `Book` (`BookNo`, `Author`, `Title`) VALUES
(321, 'Austen', 'Northanger Abbey'),
(654, 'Mitchell', 'Gone with the wind'),
(987, 'Dickens', 'Barnaby Rudge');

-- --------------------------------------------------------

--
-- Table structure for table `Borrower`
--

CREATE TABLE IF NOT EXISTS `Borrower` (
  `BorrowerNo` int(11) NOT NULL,
  `Name` varchar(20) DEFAULT NULL,
  `Department` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `Borrower`
--

INSERT INTO `Borrower` (`BorrowerNo`, `Name`, `Department`) VALUES
(123, 'Smith', 'CS and M'),
(456, 'Jones', 'Marketing'),
(789, 'Robinson', 'Film');

-- --------------------------------------------------------

--
-- Table structure for table `checklist`
--

CREATE TABLE IF NOT EXISTS `checklist` (
`checklistID` int(11) NOT NULL,
  `date` date NOT NULL,
  `eventname` varchar(100) NOT NULL,
  `place` varchar(50) NOT NULL,
  `description` varchar(2000) NOT NULL,
  `visible` tinyint(1) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `checklist`
--

INSERT INTO `checklist` (`checklistID`, `date`, `eventname`, `place`, `description`, `visible`) VALUES
(1, '2017-10-19', 'Submission of the Interim Report (two printed copies)', 'To secretaries in 4B112', 'asd', 1),
(3, '2017-10-09', 'Test checklist111222333', '3V2', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec vestibulum faucibus diam feugiat pulvinar. Duis lobortis ac elit vitae tempor. Ut tempus tellus a metus vestibulum volutpat. Sed convallis eleifend tortor. Nunc ultrices, neque non facilisis tempor, metus leo efficitur lorem, ut gravida erat tortor sed lorem. Phasellus feugiat lacinia libero, vel convallis purus accumsan a. Cras scelerisque id urna in aliquam. Mauris commodo urna pretium mi auctor facilisis.  Sed rutrum commodo arcu, sed convallis diam tristique quis. Nam semper sit amet mauris molestie volutpat. Duis augue sapien, interdum a efficitur vitae, bibendum eget dui. Nam accumsan enim tempor tempus tristique. Morbi nisi leo, rutrum id molestie a, gravida sed purus. Maecenas semper, augue non varius egestas, est est porta mauris, non sodales quam ipsum nec odio. Pellentesque eget quam tempus, elementum nunc at, maximus ipsum.', 1),
(4, '2017-10-09', 'Test checklist 111', '3V2', 'asdsadasdsa', 1),
(5, '2017-10-08', 'Test checklist   00000', '3V2', '', 1),
(7, '2017-09-28', '12454', 'sarr', '', 0),
(8, '2017-10-29', 'New checklist11', '4x5', '', 1),
(9, '2017-10-09', 'New event with description', '3f4', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec vestibulum faucibus diam feugiat pulvinar. Duis lobortis ac elit vitae tempor. Ut tempus tellus a metus vestibulum volutpat. Sed convallis eleifend tortor. Nunc ultrices, neque non facilisis tempor, metus leo efficitur lorem, ut gravida erat tortor sed lorem. Phasellus feugiat lacinia libero, vel convallis purus accumsan a. Cras scelerisque id urna in aliquam. Mauris commodo urna pretium mi auctor facilisis.  Sed rutrum commodo arcu, sed convallis diam tristique quis. Nam semper sit amet mauris molestie volutpat. Duis augue sapien, interdum a efficitur vitae, bibendum eget dui. Nam accumsan enim tempor tempus tristique. Morbi nisi leo, rutrum id molestie a, gravida sed purus. Maecenas semper, augue non varius egestas, est est porta mauris, non sodales quam ipsum nec odio. Pellentesque eget quam tempus, elementum nunc at, maximus ipsum.', 1);

-- --------------------------------------------------------

--
-- Table structure for table `interestproject`
--

CREATE TABLE IF NOT EXISTS `interestproject` (
`interestprojectID` smallint(6) NOT NULL,
  `userID` int(11) NOT NULL,
  `projectID` int(11) NOT NULL,
  `visible` tinyint(1) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `interestproject`
--

INSERT INTO `interestproject` (`interestprojectID`, `userID`, `projectID`, `visible`) VALUES
(2, 3, 18, 1),
(3, 3, 19, 1),
(22, 2, 20, 1),
(23, 3, 13, 1),
(26, 3, 28, 1),
(27, 3, 20, 0),
(28, 3, 26, 0),
(29, 3, 31, 1);

-- --------------------------------------------------------

--
-- Table structure for table `lecturer`
--

CREATE TABLE IF NOT EXISTS `lecturer` (
`lecturerID` int(11) NOT NULL,
  `name` varchar(75) NOT NULL,
  `email` varchar(75) NOT NULL,
  `department` varchar(75) NOT NULL,
  `password` varchar(75) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Loan`
--

CREATE TABLE IF NOT EXISTS `Loan` (
  `BorrowerNo` int(11) NOT NULL DEFAULT '0',
  `BookNo` int(11) NOT NULL DEFAULT '0',
  `Date_out` date DEFAULT NULL,
  `Date_back` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `Loan`
--

INSERT INTO `Loan` (`BorrowerNo`, `BookNo`, `Date_out`, `Date_back`) VALUES
(123, 321, '2011-10-02', NULL),
(123, 654, '2011-09-25', NULL),
(789, 321, '2011-09-21', '2011-09-24');

-- --------------------------------------------------------

--
-- Table structure for table `menu`
--

CREATE TABLE IF NOT EXISTS `menu` (
`menuID` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `shortIdent` varchar(25) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `menu`
--

INSERT INTO `menu` (`menuID`, `name`, `shortIdent`) VALUES
(1, 'Prawn Cocktail', 'PrawnC'),
(2, 'Melon', 'Melon'),
(3, 'Soup of the day', 'SoupOTD'),
(4, 'Octopus', 'Octo'),
(5, 'Pasta', 'Pas'),
(6, 'Steak', 'STK'),
(7, 'Ice Cream', 'ICR'),
(8, 'Apple pie', 'Appie');

-- --------------------------------------------------------

--
-- Table structure for table `project`
--

CREATE TABLE IF NOT EXISTS `project` (
`projectID` int(11) NOT NULL,
  `year` int(11) NOT NULL,
  `title` varchar(75) NOT NULL,
  `topic` varchar(150) NOT NULL,
  `compulsoryreading` varchar(300) NOT NULL,
  `description` varchar(2000) NOT NULL,
  `lecturerID` int(11) NOT NULL,
  `visible` tinyint(1) NOT NULL,
  `documentID` int(11) NOT NULL,
  `waitingtobeapproved` tinyint(1) NOT NULL,
  `checklistID` int(11) NOT NULL,
  `visitcounter` int(11) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `project`
--

INSERT INTO `project` (`projectID`, `year`, `title`, `topic`, `compulsoryreading`, `description`, `lecturerID`, `visible`, `documentID`, `waitingtobeapproved`, `checklistID`, `visitcounter`) VALUES
(1, 2017, 'testing a new project', 'computer, xml, java', '101 programming for dummies', 'A test project to test the formulary', 1, 1, 1, 1, 1, 0),
(3, 2017, 'testing a new project', 'computer, xml, java', '[101 programming for dummies]', 'A test project to test the formulary', 1, 0, 1, 0, 1, 0),
(5, 2017, 'testing a new project', '[computer, xml, java]', '[101 programming for dummies]', 'A test project to test the formulary', 1, 0, 1, 0, 1, 0),
(6, 2017, 'testing a new project', '[computer, xml, java]', '[101 programming for dummies]', 'A test project to test the formulary', 1, 0, 1, 0, 1, 0),
(7, 2017, 'testing a new project', '[computer, xml, java]', '[101 programming for dummies]', 'A test project to test the formulary', 1, 0, 1, 0, 1, 0),
(8, 2017, 'testing a new project', '[computer, xml, java]', '[101 programming for dummies]', 'A test project to test the formulary', 1, 0, 1, 0, 1, 0),
(9, 2017, 'testing a new project', 'computer, xml, java', '[101 programming for dummies]', 'A test project to test the formulary', 1, 0, 1, 0, 1, 0),
(13, 2017, 'Dissertation', '[html, css, java]', '[Meh!]', 'More meh!', 1, 1, 1, 1, 1, 0),
(14, 2017, 'testing more than one topic and readings', 'css,html,java,jsp,spring,javascript', 'book1, book2, book3,book4', 'testing', 1, 0, 1, 1, 1, 0),
(15, 2017, 'testing more than one topic and readings', 'css,html,java,jsp,spring', 'book1, book2, book3', 'testing', 1, 1, 1, 0, 1, 0),
(16, 0, '', '', 'sads,asdasd,asdasd', '', 1, 0, 1, 0, 1, 0),
(17, 2015, 'New title becuase I do not like the last one', 'asd,45', 'sads,asdasd,asdasd', '', 1, 1, 1, 0, 1, 0),
(18, 2017, 'new title 44', '1,2,3,4,5,6', '7,8,9,10,11', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec vestibulum faucibus diam feugiat pulvinar. Duis lobortis ac elit vitae tempor. Ut tempus tellus a metus vestibulum volutpat. Sed convallis eleifend tortor. Nunc ultrices, neque non facilisis tempor, metus leo efficitur lorem, ut gravida erat tortor sed lorem. Phasellus feugiat lacinia libero, vel convallis purus accumsan a. Cras scelerisque id urna in aliquam. Mauris commodo urna pretium mi auctor facilisis.  Sed rutrum commodo arcu, sed convallis diam tristique quis. Nam semper sit amet mauris molestie volutpat. Duis augue sapien, interdum a efficitur vitae, bibendum eget dui. Nam accumsan enim tempor tempus tristique. Morbi nisi leo, rutrum id molestie a, gravida sed purus. Maecenas semper, augue non varius egestas, est est porta mauris, non sodales quam ipsum nec odio. Pellentesque eget quam tempus, elementum nunc at, maximus ipsum.', 1, 1, 1, 1, 1, 0),
(19, 2017, 'Project with Lecturer ID', 'asd,qwe,rty,zxc', 'wewqe,rt,56,rew', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec vestibulum faucibus diam feugiat pulvinar. Duis lobortis ac elit vitae tempor. Ut tempus tellus a metus vestibulum volutpat. Sed convallis eleifend tortor. Nunc ultrices, neque non facilisis tempor, metus leo efficitur lorem, ut gravida erat tortor sed lorem. Phasellus feugiat lacinia libero, vel convallis purus accumsan a. Cras scelerisque id urna in aliquam. Mauris commodo urna pretium mi auctor facilisis.  Sed rutrum commodo arcu, sed convallis diam tristique quis. Nam semper sit amet mauris molestie volutpat. Duis augue sapien, interdum a efficitur vitae, bibendum eget dui. Nam accumsan enim tempor tempus tristique. Morbi nisi leo, rutrum id molestie a, gravida sed purus. Maecenas semper, augue non varius egestas, est est porta mauris, non sodales quam ipsum nec odio. Pellentesque eget quam tempus, elementum nunc at, maximus ipsum.', 1, 0, 1, 1, 1, 0),
(20, 2017, 'test with lectureID', 'asda,asdasd,23,as', 'adssad,344,asas,4', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec vestibulum faucibus diam feugiat pulvinar. Duis lobortis ac elit vitae tempor. Ut tempus tellus a metus vestibulum volutpat. Sed convallis eleifend tortor. Nunc ultrices, neque non facilisis tempor, metus leo efficitur lorem, ut gravida erat tortor sed lorem. Phasellus feugiat lacinia libero, vel convallis purus accumsan a. Cras scelerisque id urna in aliquam. Mauris commodo urna pretium mi auctor facilisis.  Sed rutrum commodo arcu, sed convallis diam tristique quis. Nam semper sit amet mauris molestie volutpat. Duis augue sapien, interdum a efficitur vitae, bibendum eget dui. Nam accumsan enim tempor tempus tristique. Morbi nisi leo, rutrum id molestie a, gravida sed purus. Maecenas semper, augue non varius egestas, est est porta mauris, non sodales quam ipsum nec odio. Pellentesque eget quam tempus, elementum nunc at, maximus ipsum.', 1, 1, 1, 1, 1, 0),
(21, 2017, '1', '124', '444', '', 15, 0, 1, 0, 1, 0),
(25, 2017, 'Test of project with lecturer data', 'html,css,java', '1,2,3,4', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec vestibulum faucibus diam feugiat pulvinar. Duis lobortis ac elit vitae tempor. Ut tempus tellus a metus vestibulum volutpat. Sed convallis eleifend tortor. Nunc ultrices, neque non facilisis tempor, metus leo efficitur lorem, ut gravida erat tortor sed lorem. Phasellus feugiat lacinia libero, vel convallis purus accumsan a. Cras scelerisque id urna in aliquam. Mauris commodo urna pretium mi auctor facilisis.  Sed rutrum commodo arcu, sed convallis diam tristique quis. Nam semper sit amet mauris molestie volutpat. Duis augue sapien, interdum a efficitur vitae, bibendum eget dui. Nam accumsan enim tempor tempus tristique. Morbi nisi leo, rutrum id molestie a, gravida sed purus. Maecenas semper, augue non varius egestas, est est porta mauris, non sodales quam ipsum nec odio. Pellentesque eget quam tempus, elementum nunc at, maximus ipsum.', 1, 0, 1, 0, 1, 0),
(26, 2017, 'My project33', 'asd', 'asdasd', 'asdasd', 2, 1, 1, 1, 1, 0),
(27, 2017, 'new project', 'html', 'html 101', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec vestibulum faucibus diam feugiat pulvinar. Duis lobortis ac elit vitae tempor. Ut tempus tellus a metus vestibulum volutpat. Sed convallis eleifend tortor. Nunc ultrices, neque non facilisis tempor, metus leo efficitur lorem, ut gravida erat tortor sed lorem. Phasellus feugiat lacinia libero, vel convallis purus accumsan a. Cras scelerisque id urna in aliquam. Mauris commodo urna pretium mi auctor facilisis.  Sed rutrum commodo arcu, sed convallis diam tristique quis. Nam semper sit amet mauris molestie volutpat. Duis augue sapien, interdum a efficitur vitae, bibendum eget dui. Nam accumsan enim tempor tempus tristique. Morbi nisi leo, rutrum id molestie a, gravida sed purus. Maecenas semper, augue non varius egestas, est est porta mauris, non sodales quam ipsum nec odio. Pellentesque eget quam tempus, elementum nunc at, maximus ipsum.', 1, 0, 1, 1, 1, 0),
(28, 2017, 'test testing the test', 'test,html,css', 'test', 'test', 1, 0, 1, 1, 1, 0),
(29, 0, 'test with lectureID5555', '', '', '', 1, 1, 1, 1, 1, 0),
(30, 2018, '1234567891011', 'computer, xml, java', '101 programming for dummies', '', 1, 1, 1, 1, 1, 0),
(31, 0, '111111', '', '', '', 1, 1, 1, 1, 1, 0),
(32, 2017, 'Test project life', 'computer, xml, java', '7,8,9,10,11', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec vestibulum faucibus diam feugiat pulvinar. Duis lobortis ac elit vitae tempor. Ut tempus tellus a metus vestibulum volutpat. Sed convallis eleifend tortor. Nunc ultrices, neque non facilisis tempor, metus leo efficitur lorem, ut gravida erat tortor sed lorem. Phasellus feugiat lacinia libero, vel convallis purus accumsan a. Cras scelerisque id urna in aliquam. Mauris commodo urna pretium mi auctor facilisis.  Sed rutrum commodo arcu, sed convallis diam tristique quis. Nam semper sit amet mauris molestie volutpat. Duis augue sapien, interdum a efficitur vitae, bibendum eget dui. Nam accumsan enim tempor tempus tristique. Morbi nisi leo, rutrum id molestie a, gravida sed purus. Maecenas semper, augue non varius egestas, est est porta mauris, non sodales quam ipsum nec odio. Pellentesque eget quam tempus, elementum nunc at, maximus ipsum.', 1, 1, 1, 1, 1, 0),
(33, 2019, 'Another test', '', '', '', 1, 1, 1, 1, 1, 0),
(34, 0, 'Am I visible?', '', '101 programming for dummies', '', 1, 1, 1, 1, 1, 0),
(35, 2017, 'for testing purpose', 'computer, xml, java', 'sads,asdasd,asdasd', 'qqqq11', 1, 1, 1, 1, 1, 0),
(36, 0, '1qwqwqqw12', 'computer, xml, java', '', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec vestibulum faucibus diam feugiat pulvinar. Duis lobortis ac elit vitae tempor. Ut tempus tellus a metus vestibulum volutpat. Sed convallis eleifend tortor. Nunc ultrices, neque non facilisis tempor, metus leo efficitur lorem, ut gravida erat tortor sed lorem. Phasellus feugiat lacinia libero, vel convallis purus accumsan a. Cras scelerisque id urna in aliquam. Mauris commodo urna pretium mi auctor facilisis.  Sed rutrum commodo arcu, sed convallis diam tristique quis. Nam semper sit amet mauris molestie volutpat. Duis augue sapien, interdum a efficitur vitae, bibendum eget dui. Nam accumsan enim tempor tempus tristique. Morbi nisi leo, rutrum id molestie a, gravida sed purus. Maecenas semper, augue non varius egestas, est est porta mauris, non sodales quam ipsum nec odio. Pellentesque eget quam tempus, elementum nunc at, maximus ipsum.', 1, 1, 1, 0, 1, 0),
(37, 0, 'tesssssst', '', '', '', 1, 1, 1, 0, 1, 0);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
`userID` int(11) NOT NULL,
  `name` varchar(150) NOT NULL,
  `username` varchar(75) NOT NULL,
  `password` varchar(100) NOT NULL,
  `email` varchar(150) NOT NULL,
  `userType` int(11) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`userID`, `name`, `username`, `password`, `email`, `userType`) VALUES
(1, 'test', 'test', 'test', 'test@test.com', 1),
(2, 'Ismael', 'Ismael', '123456', 'ismael.sanchez.leon@gmail.com', 2),
(3, 'student', 'student', 'student', 'student@student.com', 2);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `approvedproject`
--
ALTER TABLE `approvedproject`
 ADD PRIMARY KEY (`approvedprojectID`), ADD KEY `userID` (`userID`), ADD KEY `projectID` (`projectID`);

--
-- Indexes for table `Book`
--
ALTER TABLE `Book`
 ADD PRIMARY KEY (`BookNo`);

--
-- Indexes for table `Borrower`
--
ALTER TABLE `Borrower`
 ADD PRIMARY KEY (`BorrowerNo`);

--
-- Indexes for table `checklist`
--
ALTER TABLE `checklist`
 ADD PRIMARY KEY (`checklistID`);

--
-- Indexes for table `interestproject`
--
ALTER TABLE `interestproject`
 ADD PRIMARY KEY (`interestprojectID`), ADD KEY `userID` (`userID`), ADD KEY `projectID` (`projectID`);

--
-- Indexes for table `lecturer`
--
ALTER TABLE `lecturer`
 ADD PRIMARY KEY (`lecturerID`);

--
-- Indexes for table `Loan`
--
ALTER TABLE `Loan`
 ADD PRIMARY KEY (`BorrowerNo`,`BookNo`);

--
-- Indexes for table `menu`
--
ALTER TABLE `menu`
 ADD PRIMARY KEY (`menuID`);

--
-- Indexes for table `project`
--
ALTER TABLE `project`
 ADD PRIMARY KEY (`projectID`), ADD KEY `lecturerID` (`lecturerID`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
 ADD PRIMARY KEY (`userID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `approvedproject`
--
ALTER TABLE `approvedproject`
MODIFY `approvedprojectID` smallint(6) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `checklist`
--
ALTER TABLE `checklist`
MODIFY `checklistID` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=10;
--
-- AUTO_INCREMENT for table `interestproject`
--
ALTER TABLE `interestproject`
MODIFY `interestprojectID` smallint(6) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=30;
--
-- AUTO_INCREMENT for table `lecturer`
--
ALTER TABLE `lecturer`
MODIFY `lecturerID` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `menu`
--
ALTER TABLE `menu`
MODIFY `menuID` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT for table `project`
--
ALTER TABLE `project`
MODIFY `projectID` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=38;
--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
MODIFY `userID` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `approvedproject`
--
ALTER TABLE `approvedproject`
ADD CONSTRAINT `approvedproject_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`),
ADD CONSTRAINT `approvedproject_ibfk_2` FOREIGN KEY (`projectID`) REFERENCES `project` (`projectID`);

--
-- Constraints for table `interestproject`
--
ALTER TABLE `interestproject`
ADD CONSTRAINT `interestproject_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`),
ADD CONSTRAINT `interestproject_ibfk_2` FOREIGN KEY (`projectID`) REFERENCES `project` (`projectID`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
