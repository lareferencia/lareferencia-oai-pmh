<?xml version="1.0" encoding="UTF-8"?>
<!-- 

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    Developed by Paulo Graça <paulo.graca@fccn.pt>
    
    > https://www.openaire.eu/schema/repo-lit/4.0/openaire.xsd

 -->
<xsl:stylesheet
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:doc="http://www.lyncode.com/xoai"
	xmlns:datacite="http://datacite.org/schema/kernel-4"
	xmlns:dc="http://purl.org/dc/elements/1.1/"
	xmlns:dcterms="http://purl.org/dc/terms/"
	xmlns:oaire="http://namespace.openaire.eu/schema/oaire/"
	xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
	xmlns:vc="http://www.w3.org/2007/XMLSchema-versioning"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="2.0">
	<xsl:output method="xml" indent="yes" encoding="utf-8" />

	<!-- root -->
	<xsl:template match="/">
		<xsl:element name="oaire:resource">
			<xsl:namespace name="dc" select="'http://purl.org/dc/elements/1.1/'"/>
            <xsl:namespace name="oaire" select="'http://namespace.openaire.eu/schema/oaire/'"/>
			<xsl:namespace name="datacite" select="'http://datacite.org/schema/kernel-4'"/>
			<xsl:apply-templates select="*" />
		</xsl:element>
	</xsl:template>

	<!-- dc.language -->
	<xsl:template match="doc:element[@name='dc']/doc:element[@name='language']">
		<xsl:element name="dc:language">
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>
	<!-- dc.publisher -->
	<xsl:template match="doc:element[@name='dc']/doc:element[@name='publisher']">
		<xsl:element name="dc:publisher">
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>
	<!-- dc.description -->
	<xsl:template match="doc:element[@name='dc']/doc:element[@name='description']">		
		<xsl:element name="dc:description">			
			<xsl:apply-templates select="doc:field[@name!='value']"/>
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>	
	<!-- dc.format -->
	<xsl:template match="doc:element[@name='dc']/doc:element[@name='format']">
		<xsl:element name="dc:format">
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>
	<!-- dc.source -->
	<xsl:template match="doc:element[@name='dc']/doc:element[@name='source']">
		<xsl:element name="dc:source">
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>
	<!-- dc.coverage -->
	<xsl:template match="doc:element[@name='dc']/doc:element[@name='coverage']">
		<xsl:element name="dc:coverage">
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>

	<!-- datacite -->

	<!-- datacite.titles -->
	<xsl:template match="doc:element[@name='datacite']/doc:element[@name='titles']">
		<xsl:element name="datacite:titles">
			<xsl:for-each select="doc:element[@name='title']">
				<xsl:apply-templates select="." />
			</xsl:for-each>
		</xsl:element>
	</xsl:template>
	<!-- datacite.title -->
	<xsl:template match="doc:element[@name='title']">
		<xsl:element name="datacite:title">
			<xsl:apply-templates select="doc:field[@name!='value']"/>
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>
	<!-- datacite.creators -->
	<xsl:template match="doc:element[@name='datacite']/doc:element[@name='creators']">
		<xsl:element name="datacite:creators">
			<xsl:for-each select="doc:element[@name='creator']">
				<xsl:apply-templates select="." />
			</xsl:for-each>
		</xsl:element>
	</xsl:template>
	<!-- datacite.creator -->
	<xsl:template match="doc:element[@name='creator']">
		<xsl:element name="datacite:creator">
			<xsl:apply-templates select="*" />
		</xsl:element>
	</xsl:template>
	<!-- datacite.contributors -->
	<xsl:template match="doc:element[@name='datacite']/doc:element[@name='contributors']">
		<xsl:element name="datacite:contributors">
			<xsl:for-each select="doc:element[@name='contributor']">
				<xsl:apply-templates select="." />
			</xsl:for-each>
		</xsl:element>
	</xsl:template>
	<!-- datacite.creatorName -->
	<xsl:template match="doc:element[@name='creatorName']">
		<xsl:element name="datacite:creatorName">
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>		
	<!-- datacite.contributor -->
	<xsl:template match="doc:element[@name='contributor']">
		<xsl:element name="datacite:contributor">
			<xsl:apply-templates select="*" />
		</xsl:element>
	</xsl:template>
	<!-- datacite.contributorName -->
	<xsl:template match="doc:element[@name='contributorName']">
		<xsl:element name="datacite:contributorName">
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>
	<!-- datacite.givenName -->
	<xsl:template match="doc:element[@name='givenName']">
		<xsl:element name="datacite:givenName">
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>
	<!-- datacite.familyName -->
	<xsl:template match="doc:element[@name='familyName']">
		<xsl:element name="datacite:familyName">
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>
	<!-- datacite.affiliation -->
	<xsl:template match="doc:element[@name='affiliation']">
		<xsl:element name="datacite:affiliation">
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>	
	<!-- datacite:nameIdentifier -->
	<xsl:template match="doc:element[@name='nameIdentifier']">
		<xsl:element name="datacite:nameIdentifier">
			<xsl:apply-templates select="doc:field[@name!='value']"/>
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>
	<!-- datacite.alternateIdentifiers -->
	<xsl:template match="doc:element[@name='alternateIdentifiers']">
		<xsl:element name="datacite:alternateIdentifiers">
			<xsl:for-each select="doc:element[@name='alternateIdentifier']">
				<xsl:apply-templates select="." />
			</xsl:for-each>
		</xsl:element>
	</xsl:template>
	<!-- datacite.alternateIdentifier -->
	<xsl:template match="doc:element[@name='alternateIdentifier']">
		<xsl:element name="datacite:alternateIdentifier">
			<xsl:apply-templates select="doc:field[@name!='value']"/>
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>
	<!-- datacite.relatedIdentifiers -->
	<xsl:template match="doc:element[@name='relatedIdentifiers']">
		<xsl:element name="datacite:relatedIdentifiers">
			<xsl:for-each select="doc:element[@name='relatedIdentifier']">
				<xsl:apply-templates select="." />
			</xsl:for-each>
		</xsl:element>
	</xsl:template>
	<!-- datacite.relatedIdentifier -->
	<xsl:template match="doc:element[@name='relatedIdentifier']">
		<xsl:element name="datacite:relatedIdentifier">
			<xsl:apply-templates select="doc:field[@name!='value']"/>
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>
	<!-- datacite.dates -->
	<xsl:template match="doc:element[@name='datacite']/doc:element[@name='dates']">
		<xsl:element name="datacite:dates">
			<xsl:for-each select="doc:element[@name='date']">
				<xsl:apply-templates select="." />
			</xsl:for-each>
		</xsl:element>
	</xsl:template>
	<!-- datacite.date -->
	<xsl:template match="doc:element[@name='date']">
		<xsl:for-each select="*">
			<xsl:element name="datacite:date">
				<xsl:apply-templates select="." mode="datacite_date"/>
				<xsl:value-of select="doc:field[@name='value']/text()"/>
			</xsl:element>
		</xsl:for-each>
	</xsl:template>
	<!-- datacite.identifier -->
	<xsl:template match="doc:element[@name='datacite']/doc:element[@name='identifier']">
		<xsl:element name="datacite:identifier">
			<xsl:apply-templates select="doc:field[@name!='value']"/>
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>
	<!-- datacite.rights -->
	<xsl:template match="doc:element[@name='datacite']/doc:element[@name='rights']">
		<xsl:element name="datacite:rights">
			<xsl:apply-templates select="doc:field[@name!='value']"/>
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>
	<!-- datacite.subjects -->
	<xsl:template match="doc:element[@name='datacite']/doc:element[@name='subjects']">
		<xsl:element name="datacite:subjects">
			<xsl:for-each select="doc:element[@name='subject']">
				<xsl:apply-templates select="." />
			</xsl:for-each>
		</xsl:element>
	</xsl:template>
	<xsl:template match="doc:element[@name='subject']">
		<xsl:element name="datacite:subject">
			<xsl:apply-templates select="doc:field[@name!='value']"/>
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>
	<!-- datacite.sizes -->
	<xsl:template match="doc:element[@name='datacite']/doc:element[@name='sizes']">
		<xsl:element name="datacite:sizes">
			<xsl:for-each select="doc:element[@name='size']">
				<xsl:apply-templates select="." />
			</xsl:for-each>
		</xsl:element>
	</xsl:template>
	<!-- datacite.size -->
	<xsl:template match="doc:element[@name='size']">
		<xsl:element name="datacite:size">
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>
	<!-- datacite.geoLocations -->
	<xsl:template match="doc:element[@name='datacite']/doc:element[@name='geoLocations']">
		<xsl:element name="datacite:geoLocations">
			<xsl:for-each select="doc:element[@name='geoLocation']">
				<xsl:apply-templates select="." />
			</xsl:for-each>
		</xsl:element>
	</xsl:template>
	<!-- datacite.geoLocation -->
	<xsl:template match="doc:element[@name='geoLocation']">
		<xsl:element name="datacite:geoLocation">
			<xsl:apply-templates select="*" />
		</xsl:element>
	</xsl:template>
	<!-- datacite.geoLocationPoint -->
	<xsl:template match="doc:element[@name='geoLocationPoint']">
		<xsl:element name="datacite:geoLocationPoint">
			<xsl:apply-templates
				select="doc:element[@name='pointLongitude']" />
			<xsl:apply-templates
				select="doc:element[@name='pointLatitude']" />
		</xsl:element>
	</xsl:template>
	<!-- datacite.geoLocationBox -->
	<xsl:template match="doc:element[@name='geoLocationBox']">
		<xsl:element name="datacite:geoLocationBox">
			<xsl:apply-templates
				select="doc:element[@name='westBoundLongitude']" />
			<xsl:apply-templates
				select="doc:element[@name='eastBoundLongitude']" />
			<xsl:apply-templates
				select="doc:element[@name='southBoundLatitude']" />
			<xsl:apply-templates
				select="doc:element[@name='northBoundLatitude']" />
		</xsl:element>
	</xsl:template>
	<!-- datacite.geoLocationBox.westBoundLongitude -->
	<xsl:template match="doc:element[@name='westBoundLongitude']">
		<xsl:element name="datacite:westBoundLongitude">
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>
	<!-- datacite.geoLocationBox.eastBoundLongitude -->
	<xsl:template match="doc:element[@name='eastBoundLongitude']">
		<xsl:element name="datacite:eastBoundLongitude">
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>
	<!-- datacite.geoLocationBox.southBoundLatitude -->
	<xsl:template match="doc:element[@name='southBoundLatitude']">
		<xsl:element name="datacite:southBoundLatitude">
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>
	<!-- datacite.geoLocationBox.northBoundLatitude -->
	<xsl:template match="doc:element[@name='northBoundLatitude']">
		<xsl:element name="datacite:northBoundLatitude">
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>
	<!-- datacite.geoLocationPlace -->
	<xsl:template match="doc:element[@name='geoLocationPlace']">
		<xsl:element name="datacite:geoLocationPlace">
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>
	<!-- datacite.geoLocationPolygon -->
	<xsl:template match="doc:element[@name='geoLocationPolygon']">
		<xsl:element name="datacite:geoLocationPolygon">
			<xsl:apply-templates
				select="doc:element[@name='polygonPoint']" />
			<xsl:apply-templates select="*" />
		</xsl:element>
	</xsl:template>
	<!-- datacite.polygonPoint -->
	<xsl:template match="doc:element[@name='polygonPoint']">
		<xsl:element name="datacite:polygonPoint">
			<xsl:apply-templates
				select="doc:element[@name='pointLongitude']" />
			<xsl:apply-templates
				select="doc:element[@name='pointLatitude']" />
		</xsl:element>
	</xsl:template>
	<!-- datacite.inPolygonPoint -->
	<xsl:template match="doc:element[@name='inPolygonPoint']">
		<xsl:element name="datacite:inPolygonPoint">
			<xsl:apply-templates
				select="doc:element[@name='pointLongitude']" />
			<xsl:apply-templates
				select="doc:element[@name='pointLatitude']" />
		</xsl:element>
	</xsl:template>
	<!-- datacite.*.pointLongitude -->
	<xsl:template match="doc:element[@name='pointLongitude']">
		<xsl:element name="datacite:pointLongitude">
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>
	<!-- datacite.*.pointLatitude -->
	<xsl:template match="doc:element[@name='pointLatitude']">
		<xsl:element name="datacite:pointLatitude">
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>


	<!-- datacite attributes -->
	<xsl:template match="doc:field[@name='titleType']">
		<xsl:attribute name="titleType">
			<xsl:value-of select="./text()"/>
		</xsl:attribute>
	</xsl:template>	
	<xsl:template match="doc:field[@name='contributorType']">
		<xsl:attribute name="contributorType">
			<xsl:value-of select="./text()"/>
		</xsl:attribute>
	</xsl:template>
	<xsl:template match="doc:field[@name='nameType']">
		<xsl:attribute name="nameType">
			<xsl:value-of select="./text()"/>
		</xsl:attribute>		
	</xsl:template>	
	<xsl:template match="doc:field[@name='nameIdentifierScheme']">
		<xsl:attribute name="nameIdentifierScheme">
			<xsl:value-of select="./text()"/>
		</xsl:attribute>
	</xsl:template>
	<xsl:template match="doc:field[@name='schemeURI']">
		<xsl:attribute name="schemeURI">
			<xsl:value-of select="./text()"/>
		</xsl:attribute>
	</xsl:template>
	<xsl:template match="doc:field[@name='schemeType']">
		<xsl:attribute name="schemeType">
			<xsl:value-of select="./text()"/>
		</xsl:attribute>
	</xsl:template>
	<xsl:template match="doc:field[@name='relationType']">
		<xsl:attribute name="relationType">
			<xsl:value-of select="./text()"/>
		</xsl:attribute>
	</xsl:template>
	<xsl:template match="doc:field[@name='relatedMetadataScheme']">
		<xsl:attribute name="relatedMetadataScheme">
			<xsl:value-of select="./text()"/>
		</xsl:attribute>
	</xsl:template>
	<xsl:template match="doc:field[@name='resourceTypeGeneral']">
		<xsl:attribute name="resourceTypeGeneral">
			<xsl:value-of select="./text()"/>
		</xsl:attribute>
	</xsl:template>
	<xsl:template match="doc:field[@name='relatedIdentifierType']">
		<xsl:attribute name="relatedIdentifierType">
			<xsl:value-of select="./text()"/>
		</xsl:attribute>
	</xsl:template>
	<xsl:template match="doc:field[@name='alternateIdentifierType']">
		<xsl:attribute name="alternateIdentifierType">
			<xsl:value-of select="./text()"/>
		</xsl:attribute>
	</xsl:template>
	<xsl:template match="doc:field[@name='identifierType']">
		<xsl:attribute name="identifierType">
			<xsl:value-of select="./text()"/>
		</xsl:attribute>
	</xsl:template>
	<xsl:template match="doc:field[@name='rightsURI']">
		<xsl:attribute name="rightsURI">
			<xsl:value-of select="./text()"/>
		</xsl:attribute>
	</xsl:template>
	<xsl:template match="doc:field[@name='subjectScheme']">
		<xsl:attribute name="subjectScheme">
			<xsl:value-of select="./text()"/>
		</xsl:attribute>
	</xsl:template>
	<xsl:template match="doc:field[@name='valueURI']">
		<xsl:attribute name="valueURI">
			<xsl:value-of select="./text()"/>
		</xsl:attribute>
	</xsl:template>
	<xsl:template match="doc:element" mode="datacite_date">
		<xsl:attribute name="dateType">
			<xsl:value-of select="@name"/>
		</xsl:attribute>
	</xsl:template>




	<!-- oaire -->


	<!-- oaire.resourceType -->
	<xsl:template match="doc:element[@name='oaire']/doc:element[@name='resourceType']">
		<xsl:element name="oaire:resourceType">
			<xsl:apply-templates select="doc:field[@name!='value']" />
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>
	<!-- oaire.fundingReferences -->
	<xsl:template match="doc:element[@name='oaire']/doc:element[@name='fundingReferences']">
		<xsl:element name="oaire:fundingReferences">
			<xsl:for-each select="doc:element[@name='fundingReference']">
				<xsl:apply-templates select="." />
			</xsl:for-each>
		</xsl:element>
	</xsl:template>
	
	<!-- oaire.fundingReference -->
	<xsl:template match="doc:element[@name='fundingReference']">
		<xsl:element name="oaire:fundingReference">
			<xsl:apply-templates select="*" />
		</xsl:element>
	</xsl:template>
	
	<xsl:template match="doc:element[@name='funderName']">
		<xsl:element name="oaire:funderName">
			<xsl:element name="element">
				<xsl:attribute name="name">
               <xsl:text>funderName</xsl:text>
            </xsl:attribute>
				<xsl:value-of select="doc:field[@name='value']/text()"/>
			</xsl:element>
		</xsl:element>
	</xsl:template>	
	
	<xsl:template match="doc:element[@name='fundingStream']">
		<xsl:element name="oaire:fundingStream">
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template match="doc:element[@name='awardNumber']">
		<xsl:element name="oaire:awardNumber">
			<xsl:apply-templates select="doc:field[@name!='value']" />
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template match="doc:element[@name='awardTitle']">
		<xsl:element name="oaire:awardTitle">
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>
	<!-- oaire:funderIdentifier -->
	<xsl:template match="doc:element[@name='funderIdentifier']">
		<xsl:element name="oaire:funderIdentifier">
			<xsl:apply-templates select="doc:field[@name!='value']" />
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>
	<!-- oaire.licenseCondition -->
	<xsl:template match="doc:element[@name='licenseCondition']">
		<xsl:element name="oaire:licenseCondition">
			<xsl:apply-templates select="doc:field[@name!='value']" />
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>
	<!-- oaire.version -->
	<xsl:template match="doc:element[@name='oaire']/doc:element[@name='version']">
		<xsl:element name="oaire:version">
			<xsl:apply-templates select="doc:field[@name!='value']" />
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>
	<!-- oaire.file -->
	<xsl:template match="doc:element[@name='oaire']/doc:element[@name='files']">
		<xsl:apply-templates select="*" />
	</xsl:template>	
	<xsl:template match="doc:element[@name='file']">
		<xsl:element name="oaire:file">
			<xsl:apply-templates select="doc:field[@name!='value']" />
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>
	<!-- oaire.citationTitle -->
	<xsl:template match="doc:element[@name='oaire']/doc:element[@name='citationTitle']">
		<xsl:element name="oaire:citationTitle">
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>
	<!-- oaire.citationVolume -->
	<xsl:template match="doc:element[@name='oaire']/doc:element[@name='citationVolume']">
		<xsl:element name="oaire:citationVolume">
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>
	<!-- oaire.citationIssue -->
	<xsl:template match="doc:element[@name='oaire']/doc:element[@name='citationIssue']">
		<xsl:element name="oaire:citationIssue">
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>
	<!-- oaire.citationStartPage -->
	<xsl:template match="doc:element[@name='oaire']/doc:element[@name='citationStartPage']">
		<xsl:element name="oaire:citationStartPage">
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>
	<!-- oaire.citationEndPage -->
	<xsl:template match="doc:element[@name='oaire']/doc:element[@name='citationEndPage']">
		<xsl:element name="oaire:citationEndPage">
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>
	<!-- oaire.citationEdition -->
	<xsl:template match="doc:element[@name='oaire']/doc:element[@name='citationEdition']">
		<xsl:element name="oaire:citationEdition">
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>
	<!-- oaire.citationConferencePlace -->
	<xsl:template match="doc:element[@name='oaire']/doc:element[@name='citationConferencePlace']">
		<xsl:element name="oaire:citationConferencePlace">
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>
	<!-- oaire.citationConferenceDate -->
	<xsl:template match="doc:element[@name='oaire']/doc:element[@name='citationConferenceDate']">
		<xsl:element name="oaire:citationConferenceDate">
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>

	<!-- oaire attributes -->
	<xsl:template match="doc:field[@name='awardURI']">
		<xsl:attribute name="awardURI">
			<xsl:value-of select="./text()"/>
		</xsl:attribute>
	</xsl:template>	
	<xsl:template match="doc:field[@name='funderIdentifierType']">
		<xsl:attribute name="funderIdentifierType">
			<xsl:value-of select="./text()"/>
		</xsl:attribute>
	</xsl:template>	
	<xsl:template match="doc:field[@name='uri']">
		<xsl:attribute name="uri">
			<xsl:value-of select="./text()"/>
		</xsl:attribute>
	</xsl:template>
	<xsl:template match="doc:field[@name='startDate']">
		<xsl:attribute name="startDate">
			<xsl:value-of select="./text()"/>
		</xsl:attribute>
	</xsl:template>
	<xsl:template match="doc:field[@name='mimeType']">
		<xsl:attribute name="mimeType">
			<xsl:value-of select="./text()"/>
		</xsl:attribute>
	</xsl:template>
	<xsl:template match="doc:field[@name='accessRightsURI']">
		<xsl:attribute name="accessRightsURI">
			<xsl:value-of select="./text()"/>
		</xsl:attribute>
	</xsl:template>
	<xsl:template match="doc:field[@name='objectType']">
		<xsl:attribute name="objectType">
			<xsl:value-of select="./text()"/>
		</xsl:attribute>
	</xsl:template>

	<!-- dcterms -->
	<xsl:template match="doc:element[@name='oaire']/doc:element[@name='audience']">
		<xsl:element name="dcterms:audience">
			<xsl:value-of select="doc:field[@name='value']/text()"/>
		</xsl:element>
	</xsl:template>


	<!-- xml attributes -->	
	<xsl:template match="doc:field[@name='lang']">
		<xsl:attribute name="xml:lang">
			<xsl:value-of select="./text()"/>
		</xsl:attribute>
	</xsl:template>


	<!-- ignore all non specified text values or attributes -->
	<xsl:template match="text()|@*" />
</xsl:stylesheet>
