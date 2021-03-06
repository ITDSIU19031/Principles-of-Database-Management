USE [master]
GO
/****** Object:  Database [Lab1]    Script Date: 3/10/2021 10:01:46 AM ******/
CREATE DATABASE [Lab1]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'Lab1', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL15.SQLEXPRESS\MSSQL\DATA\Lab1.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'Lab1_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL15.SQLEXPRESS\MSSQL\DATA\Lab1_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT
GO
ALTER DATABASE [Lab1] SET COMPATIBILITY_LEVEL = 150
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [Lab1].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [Lab1] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [Lab1] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [Lab1] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [Lab1] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [Lab1] SET ARITHABORT OFF 
GO
ALTER DATABASE [Lab1] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [Lab1] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [Lab1] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [Lab1] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [Lab1] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [Lab1] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [Lab1] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [Lab1] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [Lab1] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [Lab1] SET  DISABLE_BROKER 
GO
ALTER DATABASE [Lab1] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [Lab1] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [Lab1] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [Lab1] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [Lab1] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [Lab1] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [Lab1] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [Lab1] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [Lab1] SET  MULTI_USER 
GO
ALTER DATABASE [Lab1] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [Lab1] SET DB_CHAINING OFF 
GO
ALTER DATABASE [Lab1] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [Lab1] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [Lab1] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [Lab1] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
ALTER DATABASE [Lab1] SET QUERY_STORE = OFF
GO
USE [Lab1]
GO
/****** Object:  Table [dbo].[Course]    Script Date: 3/10/2021 10:01:47 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Course](
	[Cid] [varchar](10) NULL,
	[CName] [varchar](30) NULL,
	[Credit] [int] NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Enrolled]    Script Date: 3/10/2021 10:01:47 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Enrolled](
	[Sid] [varchar](10) NULL,
	[Cid] [varchar](12) NULL,
	[Day] [date] NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Student]    Script Date: 3/10/2021 10:01:47 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Student](
	[Sid] [varchar](10) NULL,
	[SName] [varchar](30) NULL,
	[Dob] [date] NULL,
	[ADDRESS] [varchar](50) NULL,
	[IDCARD] [varchar](12) NULL
) ON [PRIMARY]
GO
USE [master]
GO
ALTER DATABASE [Lab1] SET  READ_WRITE 
GO
