<?xml version="1.0" encoding="UTF-8" ?>
<!--


    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/
        Developed by DSpace @ Lyncode <dspace@lyncode.com>

        > http://www.openarchives.org/OAI/2.0/oai_dc.xsd

 -->
<xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:doc="http://www.lyncode.com/xoai"
        version="1.0">
        <xsl:output omit-xml-declaration="yes" method="xml" indent="yes" />

        <xsl:template match="/">
                <oai_dc:dc xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/"
                        xmlns:dc="http://purl.org/dc/elements/1.1/"
                        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                        xsi:schemaLocation="http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd">
                        <!-- dc.title -->
                        <xsl:for-each select="doc:metadata/doc:element[@name='datacite']/doc:element[@name='titles']/doc:element[@name='title']/doc:field[@name='value']">
                                <dc:title><xsl:value-of select="." /></dc:title>
                        </xsl:for-each>
                        <!-- dc.creator -->
                        <xsl:for-each select="doc:metadata/doc:element[@name='datacite']/doc:element[@name='creators']/doc:element[@name='creator']/doc:element[@name='creatorName']/doc:field[@name='value']">
                                <dc:creator><xsl:value-of select="." /></dc:creator>
                        </xsl:for-each>
                        <!-- dc.contributor -->
                        <xsl:for-each select="doc:metadata/doc:element[@name='datacite']/doc:element[@name='contributors']/doc:element[@name='contributor']/doc:element[@name='contributorName']/doc:field[@name='value']">
                                <dc:contributor><xsl:value-of select="." /></dc:contributor>
                        </xsl:for-each>
                        <!-- dc.subject -->
                        <xsl:for-each select="doc:metadata/doc:element[@name='datacite']/doc:element[@name='subjects']/doc:element[@name='subject']/doc:field[@name='value']">
                                <dc:subject><xsl:value-of select="." /></dc:subject>
                        </xsl:for-each>
                        <!-- dc.description -->
                        <xsl:for-each select="doc:metadata/doc:element[@name='dc']/doc:element[@name='description']/doc:field[@name='value']">
                                <dc:description><xsl:value-of select="." /></dc:description>
                        </xsl:for-each>
                        <!-- dc.date -->
                        <xsl:for-each select="doc:metadata/doc:element[@name='datacite']/doc:element[@name='dates']/doc:element[@name='date']/doc:element/doc:field[@name='value']">
                                <dc:date><xsl:value-of select="." /></dc:date>
                        </xsl:for-each>
                        <!-- dc.type -->
                        <xsl:for-each select="doc:metadata/doc:element[@name='oaire']/doc:element[@name='resourceType']/doc:field[@name='value']">
                                <dc:type><xsl:value-of select="." /></dc:type>
                        </xsl:for-each>
                        <!-- dc.identifier -->
                        <xsl:for-each select="doc:metadata/doc:element[@name='datacite']/doc:element[@name='identifier']/doc:field[@name='value']">
                                <dc:identifier><xsl:value-of select="." /></dc:identifier>
                        </xsl:for-each>
                        <!-- dc.identifier.* (alternative) -->
                        <xsl:for-each select="doc:metadata/doc:element[@name='datacite']/doc:element[@name='alternateIdentifiers']/doc:element[@name='alternateIdentifier']/doc:field[@name='value']">
                                <dc:identifier><xsl:value-of select="." /></dc:identifier>
                        </xsl:for-each>						
                        <!-- dc.language -->
                        <xsl:for-each select="doc:metadata/doc:element[@name='dc']/doc:element[@name='language']/doc:field[@name='value']">
                                <dc:language><xsl:value-of select="." /></dc:language>
                        </xsl:for-each>
                        <!-- dc.relation -->
                        <xsl:for-each select="doc:metadata/doc:element[@name='datacite']/doc:element[@name='relatedIdentifiers']/doc:element[@name='relatedIdentifier']/doc:field[@name='value']">
                                <dc:relation><xsl:value-of select="." /></dc:relation>
                        </xsl:for-each>
                        <!-- dc.rights -->
                        <xsl:for-each select="doc:metadata/doc:element[@name='datacite']/doc:element[@name='rights']/doc:field[@name='value']">
                                <dc:rights><xsl:value-of select="." /></dc:rights>
                        </xsl:for-each>
                        <!-- dc.format -->
                        <xsl:for-each select="doc:metadata/doc:element[@name='dc']/doc:element[@name='format']/doc:field[@name='value']">
                                <dc:format><xsl:value-of select="." /></dc:format>
                        </xsl:for-each>
                        <!-- dc.coverage -->
                        <xsl:for-each select="doc:metadata/doc:element[@name='dc']/doc:element[@name='coverage']/doc:field[@name='value']">
                                <dc:coverage><xsl:value-of select="." /></dc:coverage>
                        </xsl:for-each>
                        <!-- dc.publisher -->
                        <xsl:for-each select="doc:metadata/doc:element[@name='dc']/doc:element[@name='publisher']/doc:field[@name='value']">
                                <dc:publisher><xsl:value-of select="." /></dc:publisher>
                        </xsl:for-each>

                        <!-- dc.source -->
                        <xsl:for-each select="doc:metadata/doc:element[@name='dc']/doc:element[@name='source']/doc:field[@name='value']">
                              	<dc:source><xsl:value-of select="." /></dc:source>
                        </xsl:for-each>

                        <about>
                        <provenance>
       			<xsl:variable name="harvestDate" select="doc:metadata/doc:element[@name='repository']/doc:field[@name='harvestDate']"/>
			<xsl:variable name="recordStatus" select="doc:metadata/doc:element[@name='repository']/doc:field[@name='altered']"/>
                        <xsl:variable name="repositoryFullName" select="doc:metadata/doc:element[@name='repository']/doc:field[@name='name']"/>
                        <xsl:variable name="repoNameLength" select="string-length(tokenize(//*[contains(text(),'reponame')], ':')[2])"/>
                        <xsl:variable name="openDoarId" select="tokenize(doc:metadata/doc:element[@name='repository']/doc:field[@name='repositoryID'], ':')[2]"/>
                        <originDescription altered="{$recordStatus}" harvestDate="{$harvestDate}">           
                	        <baseURL><xsl:value-of select="doc:metadata/doc:element[@name='repository']/doc:field[@name='baseURL']"/></baseURL>
				<identifier><xsl:value-of select="doc:metadata/doc:element[@name='others']/doc:field[@name='identifier']"/></identifier>
				<datestamp><xsl:value-of select="doc:metadata/doc:element[@name='others']/doc:field[@name='lastModifyDate']"/></datestamp>
			        <metadataNamespace><xsl:text>http://www.openarchives.org/OAI/2.0/oai_dc/</xsl:text></metadataNamespace>
                                <xsl:choose>
                                        <xsl:when test="matches($openDoarId,'\d+')">
                      		                <repositoryID><xsl:value-of select="doc:metadata/doc:element[@name='repository']/doc:field[@name='repositoryID']"/></repositoryID>
                                        </xsl:when>
                                        <xsl:otherwise>
                                                <repositoryID></repositoryID>
                                        </xsl:otherwise>
                                </xsl:choose>
  			        <xsl:choose>
  					<xsl:when test="string-length(normalize-space($repositoryFullName))>3">
	                        		<repositoryName><xsl:value-of select="$repositoryFullName"/></repositoryName>
					</xsl:when>
                                        <xsl:otherwise>
                                                <xsl:if test="$repoNameLength!=0">
                                                        <repositoryName><xsl:value-of select="concat(tokenize(//*[contains(text(),'reponame')], ':')[2], concat(' - ', tokenize(//*[contains(text(),'instname')], ':')[2]))"/></repositoryName>
                                                </xsl:if>
                                                <xsl:if test="repoNameLength=0">
                                                        <repositoryName></repositoryName>
				                </xsl:if>
                                        </xsl:otherwise>        
                                </xsl:choose>
                        </originDescription>
                        </provenance>
                        </about>
                </oai_dc:dc>
        </xsl:template>
</xsl:stylesheet>
